package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeID,Long ID);
    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
}
