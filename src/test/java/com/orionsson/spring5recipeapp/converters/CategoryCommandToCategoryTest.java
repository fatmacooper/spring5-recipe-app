package com.orionsson.spring5recipeapp.converters;

import com.orionsson.spring5recipeapp.commands.CategoryCommand;
import com.orionsson.spring5recipeapp.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {
    CategoryCommandToCategory converter;
    public static final Long ID_VALUE = 1l;
    public static final String DESCRIPTION = "description";

    @Before
    public void setUp() throws Exception {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() throws Exception{
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject(){
        assertNotNull(converter.convert(new CategoryCommand()));
    }
    @Test
    public void convert() {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setDescription(DESCRIPTION);

        //when
        Category category = converter.convert(categoryCommand);
        assertNotNull(category);
        assertEquals(category.getId(),ID_VALUE);
        assertEquals(category.getDescription(),DESCRIPTION);
    }
}