package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.commands.IngredientCommand;
import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.services.IngredientService;
import com.orionsson.spring5recipeapp.services.RecipeService;
import com.orionsson.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }
    @GetMapping
    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model){
        log.debug("Getting ingredient list for recipe id: " + id);
        model.addAttribute("recipe",recipeService.findCommandById(new Long(id)));
        return "recipe/ingredient/list";
    }
    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{id}/show")
    public String showIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        log.debug("Show ingredient for recipe id: " + recipeId + "and ingredient id: " + id);
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(new Long(recipeId),new Long(id)));
        return "recipe/ingredient/show";
    }
    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),Long.valueOf(id)));
        model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }
    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,@PathVariable String id){
        ingredientService.deleteById(Long.valueOf(recipeId),Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/new")
    public String newRecipeIngredients(@PathVariable String recipeId, Model model){
        RecipeCommand recipeCommand = recipeService.findCommandById(new Long(recipeId));
        //todo raise error when recipe cannot be found
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        model.addAttribute("ingredient",ingredientCommand);
        //init uom
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }
    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("saved recipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());
        return "redirect:/recipe/" +savedCommand.getRecipeId() + "/ingredients/" + savedCommand.getId() + "/show";
    }
}
