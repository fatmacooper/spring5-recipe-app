package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.exceptions.NotFoundException;
import com.orionsson.spring5recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RecipeController {
    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
    private final RecipeService recipeService;
    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @InitBinder
    public void InitBinder(WebDataBinder webDataBinder){
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findById(id));
        return "recipe/show";
    }
    @GetMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_RECIPEFORM_URL;
    }
    @GetMapping("recipe/{id}/update")
    public  Mono<String> updateRecipe(@PathVariable String id, Model model){
        return recipeService.findCommandById(id).
                doOnNext(recipeCommand -> model.addAttribute("recipe",recipeCommand)).
                then(Mono.just(RECIPE_RECIPEFORM_URL));
    }
    @PostMapping("recipe")
    public Mono<String> saveOrUpdate(@ModelAttribute("recipe") RecipeCommand command){
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return Mono.just(RECIPE_RECIPEFORM_URL);
        }
        return recipeService.saveRecipeCommand(command).map(
              savedCommand->{
                  String ID = savedCommand.getId();
                  return "redirect:/recipe/" + savedCommand.getId() + "/show";
              }
        );
    }
    @GetMapping("recipe/{id}/delete")
    public   Mono<String> deleteRecipe(@PathVariable String id){
        log.debug("Deleting id: " + id);
        //recipeService.deleteById(id);
        return recipeService.deleteById(id).then(Mono.just("redirect:/"));
        //return Rendering.redirectTo("redirect:/").build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public Mono<String> handleNotFound(Exception exception, Model model){
        log.error("Handling not found exception");
        log.error(exception.getMessage());
        model.addAttribute("exception",exception);
        return Mono.just("404error");
    }
}
