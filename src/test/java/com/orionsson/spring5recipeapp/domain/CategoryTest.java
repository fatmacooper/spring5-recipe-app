package com.orionsson.spring5recipeapp.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {
    private Category category;
    @Before
    public void setUp(){
        category = new Category();
    }
    @Test
    public void getId() throws Exception{
        String id = "4";
        category.setId(id);
        assertEquals(id,category.getId());
    }

    @Test
    public void getDescription() throws Exception{
    }

    @Test
    public void getRecipes() throws Exception{
    }
}