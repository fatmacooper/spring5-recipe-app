package com.orionsson.spring5recipeapp.commands;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotesCommand {
    private Long id;
    private String recipeNotes;

    public Long getId() {
        return this.id;
    }

    public String getRecipeNotes() {
        return this.recipeNotes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipeNotes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }
}
