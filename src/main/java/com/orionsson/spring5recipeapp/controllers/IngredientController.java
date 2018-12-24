package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.services.IngredientService;
import com.orionsson.spring5recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
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
}
