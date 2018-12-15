package com.orionsson.spring5recipeapp.repositories;

import com.orionsson.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,Long> {
}
