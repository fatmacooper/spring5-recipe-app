package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.orionsson.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;
    @Mock
    RecipeReactiveRepository recipeReactiveRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeReactiveRepository,recipeCommandToRecipe,recipeToRecipeCommand);
    }

    @Test
    public void getRecipesTest() throws Exception{
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));
        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(recipes.size(),1);
        verify(recipeReactiveRepository,times(1)).findAll();
        verify(recipeReactiveRepository,never()).findById(anyString());
    }

    @Test
    public void getRecipeByIdTest() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNotNull("Recipe is not null", recipeReturned);
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId("1");
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1").block();

        assertNotNull("Null recipe returned",commandById);
        verify(recipeReactiveRepository,times(1)).findById(anyString());
        verify(recipeReactiveRepository,never()).findAll();
    }

    @Test
    public void deleteRecipeByIdTest() throws Exception{
        //given
        String idToDelete = "2";

        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        //when
        recipeService.deleteById(idToDelete);

        //no when, since method has void return type
        verify(recipeReactiveRepository,times(1)).deleteById(anyString());
    }
}