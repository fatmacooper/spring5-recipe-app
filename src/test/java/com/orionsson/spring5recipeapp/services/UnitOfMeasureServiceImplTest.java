package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import com.orionsson.spring5recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUoms() {
        //given
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("1");
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("2");
        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(uom1,uom2));

        //when
        List<UnitOfMeasureCommand> unitOfMeasureCommands = service.listAllUoms().collectList().block();

        assertEquals(unitOfMeasureCommands.size(),2);
        verify(unitOfMeasureReactiveRepository,times(1)).findAll();
    }
}