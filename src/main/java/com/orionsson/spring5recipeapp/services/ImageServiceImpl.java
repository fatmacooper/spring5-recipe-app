package com.orionsson.spring5recipeapp.services;

import com.orionsson.spring5recipeapp.domain.Recipe;
import com.orionsson.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

            Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId).map(recipe -> {
                try{
                    Byte[] byteObjects = new Byte[file.getBytes().length];
                    int i = 0;
                    for(byte b: file.getBytes()){
                        byteObjects[i++] = b;
                    }
                    recipe.setImage(byteObjects);
                    return  recipe;
                }catch(IOException e){
                    log.debug("Error occured",e);
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });

        recipeReactiveRepository.save(recipeMono.block()).block();
        return Mono.empty();
    }
}