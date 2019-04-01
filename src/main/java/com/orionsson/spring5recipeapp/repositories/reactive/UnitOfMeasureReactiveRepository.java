package com.orionsson.spring5recipeapp.repositories.reactive;

import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository <UnitOfMeasure,String>{
}
