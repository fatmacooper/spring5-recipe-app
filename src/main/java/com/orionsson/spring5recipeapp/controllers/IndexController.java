package com.orionsson.spring5recipeapp.controllers;

import com.orionsson.spring5recipeapp.domain.Category;
import com.orionsson.spring5recipeapp.domain.UnitOfMeasure;
import com.orionsson.spring5recipeapp.repositories.CategoryRepository;
import com.orionsson.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"","/","/index"})
    public String getIndex(){
        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByUom("Teaspoon");
        System.out.println("The cat ID is " + categoryOptional.get().getId());
        System.out.println("The uom ID is " + unitOfMeasureOptional.get().getId());
        return "index";
    }
}
