package com.orionsson.spring5recipeapp.controllers;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class IndexController {

    @RequestMapping({"","/","index.html"})
    public String getIndex(){
        return "index";
    }
}
