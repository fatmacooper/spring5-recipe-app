package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.commands.IngredientCommand;
import com.orionsson.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.services.IngredientService;
import com.orionsson.spring5recipeapp.services.RecipeService;
import com.orionsson.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;
    private WebDataBinder webDataBinder;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }
    @InitBinder
    public void InitBinder(WebDataBinder webDataBinder){this.webDataBinder = webDataBinder;}
    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model){
        log.debug("Getting ingredient list for recipe id: " + id);
        model.addAttribute("recipe",recipeService.findCommandById(id));
        return "recipe/ingredient/list";
    }
    @GetMapping("/recipe/{recipeId}/ingredients/{id}/show")
    public String showIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        log.debug("Show ingredient for recipe id: " + recipeId + "and ingredient id: " + id);
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(recipeId,id));
        return "recipe/ingredient/show";
    }
    @GetMapping("/recipe/{recipeId}/ingredients/new")
    public Mono<String> newRecipeIngredients(@PathVariable String recipeId, Model model){
        return recipeService.findCommandById(recipeId).map(e -> {
            IngredientCommand ingredientCommand = new IngredientCommand();
            ingredientCommand.setRecipeId(e.getId());
            model.addAttribute("ingredient",ingredientCommand);
            //init uom
            ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
            return "recipe/ingredient/ingredientform";
        });
    }
    @GetMapping("recipe/{recipeId}/ingredients/{id}/update")
    public Mono<String> updateRecipeIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        Mono<IngredientCommand> command = ingredientService.findByRecipeIdAndIngredientId(recipeId,id);
        return command.map(e->{
            model.addAttribute("ingredient",e);
            return "recipe/ingredient/ingredientform";
        });
    }
    @PostMapping("/recipe/{recipeId}/ingredient")
    public Mono<String> saveOrUpdate(@ModelAttribute("ingredient") IngredientCommand command){
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return Mono.just("/recipe/ingredient/ingredientform");
        }
        return ingredientService.saveIngredientCommand(command).map(e->{
            String ID = e.getId();
            return "redirect:/recipe/" +command.getRecipeId() + "/ingredients/" + e.getId() + "/show";
        });
    }
    @GetMapping("/recipe/{recipeId}/ingredients/{id}/delete")
    public Mono<String> deleteRecipeIngredient(@PathVariable String recipeId,@PathVariable String id){
        return ingredientService.deleteById(recipeId,id).then(Mono.just("redirect:/recipe/" + recipeId + "/ingredients"));
    }
    @ModelAttribute("uomList")
    public Flux<UnitOfMeasureCommand> popuplateUomList(){
        return unitOfMeasureService.listAllUoms();
    }
}