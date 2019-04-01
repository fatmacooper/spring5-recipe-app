package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.commands.IngredientCommand;
import com.orionsson.spring5recipeapp.commands.RecipeCommand;
import com.orionsson.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.services.IngredientService;
import com.orionsson.spring5recipeapp.services.RecipeService;
import com.orionsson.spring5recipeapp.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;

    MockMvc mockMvc;
    IngredientController ingredientController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientController = new IngredientController(recipeService,ingredientService,unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }
    @Test
    public void testListIngredients() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");
        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        //then
        verify(recipeService, times(1)).findCommandById(anyString());
    }
    @Test
    public void testShowIngredients() throws Exception{
        //given
        IngredientCommand command = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/ingredients/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        //then
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(),anyString());
    }
    @Test
    public void testUpdateIngredientForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

        //then
        mockMvc.perform(get("/recipe/1/ingredients/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }
    @Test
    public void testDeleteRecipe() throws Exception{
        mockMvc.perform(get("/recipe/1/ingredients/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService,times(1)).deleteById(anyString(),anyString());
    }
    @Test
    public void testNewIngredientForm() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");
        //when
        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.just(new UnitOfMeasureCommand()));
        //then
        mockMvc.perform(get("/recipe/1/ingredients/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        verify(recipeService,times(1)).findCommandById(anyString());
    }
    @Test
    public void testSaveOrUpdate() throws Exception{
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("3");
        ingredientCommand.setRecipeId("2");

        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);

        //then
        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description","some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredients/3/show"));
    }
}