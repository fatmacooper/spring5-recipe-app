package com.orionsson.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by jt on 7/3/17.
 */
public interface ImageService {

    void saveImageFile(Long recipeId, MultipartFile file);
}