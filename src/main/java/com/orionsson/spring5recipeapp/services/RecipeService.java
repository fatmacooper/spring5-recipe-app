package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> getRecipes();
    Mono<Recipe> findById(String ID);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
    Mono<RecipeCommand> findCommandById(String ID);
    Mono<Void> deleteById(String ID);
}