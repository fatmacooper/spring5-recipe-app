package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUoms();
}
