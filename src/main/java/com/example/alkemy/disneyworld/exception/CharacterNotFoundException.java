package com.example.alkemy.disneyworld.exception;

public class CharacterNotFoundException extends NotFoundException {

    private static final String DESCRIPTION = "Character Not Found";

    public CharacterNotFoundException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
