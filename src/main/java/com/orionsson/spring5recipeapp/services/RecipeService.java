package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
