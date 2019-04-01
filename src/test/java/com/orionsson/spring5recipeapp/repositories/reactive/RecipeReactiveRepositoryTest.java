package com.orionsson.spring5recipeapp.repositories.reactive;

import com.orionsson.spring5recipeapp.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;
    @Before
    public void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }
    @Test
    public void testSaveRecipe(){
        Recipe recipe = new Recipe();
        recipe.setDescription("my beatiful recipe");
        recipeReactiveRepository.save(recipe).block();

        Long count = recipeReactiveRepository.count().block();

        assertEquals(Long.valueOf(1),count);
    }
}