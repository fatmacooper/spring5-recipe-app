package com.orionsson.spring5recipeapp.repositories.reactive;

import com.orionsson.spring5recipeapp.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category,String> {
    Mono<Category> findByDescription(String description);
}
