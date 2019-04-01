package com.orionsson.spring5recipeapp.repositories.reactive;

import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository <UnitOfMeasure,String>{
    Mono<UnitOfMeasure> findUnitOfMeasureByUom(String uom);
}
