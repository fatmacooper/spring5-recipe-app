package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
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
        if(recipeOptional == null){
            throw new RuntimeException("Recipe Not Found");
        }
        return recipeOptional.get();
    }
}
