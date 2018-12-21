package com.orionsson.spring5recipeapp.repositories;

import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureTestIT {
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Test
    //@DirtiesContext
    public void findByDescription() throws Exception{
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUom("Teaspoon");
        assertEquals("Teaspoon",uomOptional.get().getUom());
    }

    @Test
    public void findByDescriptionCup() throws Exception{
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUom("Cup");
        assertEquals("Cup",uomOptional.get().getUom());
    }
}