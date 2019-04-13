package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.IngredientCommand;
import com.orionsson.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.orionsson.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.orionsson.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.domain.Ingredient;
import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import com.orionsson.spring5recipeapp.repositories.UnitOfMeasureRepository;
import com.orionsson.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;


    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });

        /*Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()){
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map( ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipeId);

        return ingredientCommandOptional.get();*/
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Optional<UnitOfMeasure> uomOpt = unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId());
        if(uomOpt == null){
            new RuntimeException("UOM NOT FOUND");
        }
        UnitOfMeasure uom = uomOpt.get();
        command.setUnitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(uom));

        return recipeReactiveRepository
                .findById(command.getRecipeId()).map(recipe->{
                    if(recipe == null){
                        return new Recipe();
                    }
                    Optional<Ingredient> ingredientOptional = recipe
                            .getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(command.getId()))
                            .findFirst();

                    if (ingredientOptional.isPresent()) {
                        Ingredient ingredientFound = ingredientOptional.get();
                        ingredientFound.setDescription(command.getDescription());
                        ingredientFound.setAmount(command.getAmount());
                        ingredientFound.setUnitOfMeasure(uom);

                    } else {
                        //add new Ingredient
                        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                        recipe.addIngredient(ingredient);
                    }
                    return recipe;
                }).flatMap(recipeReactiveRepository::save)
                .map(savedRecipe -> {
                        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                                .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                                .findFirst();
                        //check by description
                        if (!savedIngredientOptional.isPresent()) {
                            //not totally safe... But best guess
                            savedIngredientOptional = savedRecipe.getIngredients().stream()
                                    .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                                    .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                                    .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().
                                            equals(uom.getId()))
                                    .findFirst();
                        }
                        //enhance with id value
                        IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
                        ingredientCommandSaved.setRecipeId(savedRecipe.getId());
                        return ingredientCommandSaved;
                    }
                );
    }
    @Override
    public Mono<Recipe> deleteById(String recipeID, String ID) {
      return recipeReactiveRepository.findById(recipeID).flatMap(recipe -> {
            if(recipe != null){
                Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(ID)).findFirst();
                if(ingredientOptional.isPresent()){
                    recipe.getIngredients().remove(ingredientOptional.get());
                    return recipeReactiveRepository.save(recipe);
                }
            } else {
                log.debug("Recipe Id Not found. Id:" + recipeID);
            }
            return Mono.just(recipe);
        });
    }
}
