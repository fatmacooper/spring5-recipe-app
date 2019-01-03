package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.orionsson.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.exceptions.NotFoundException;
import com.orionsson.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("The log method is at RecipeServiceImpl. ");
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long ID) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ID);
        if(!recipeOptional.isPresent()){
            throw new NotFoundException("Recipe Not Found. For Id value: " + ID.toString());
        }
        return recipeOptional.get();
    }

    @Override
    public RecipeCommand findCommandById(Long ID) {
        return recipeToRecipeCommand.convert(findById(ID));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long ID) {
        recipeRepository.deleteById(ID);
    }
}
