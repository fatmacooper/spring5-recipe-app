package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.orionsson.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("The log method is at RecipeServiceImpl. ");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String ID) {
        return recipeReactiveRepository.findById(ID);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String ID) {

        return recipeReactiveRepository.findById(ID).map(
                recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
                    recipeCommand.getIngredients().forEach(rc->{
                        rc.setRecipeId(recipeCommand.getId());
                    });
                    return recipeCommand;
                });
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        return recipeReactiveRepository.
                save(recipeCommandToRecipe.convert(command)).
                map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String ID){
        recipeReactiveRepository.deleteById(ID).block();
        return Mono.empty();
    }
}
