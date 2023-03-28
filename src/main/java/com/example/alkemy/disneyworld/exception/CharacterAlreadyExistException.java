package com.example.alkemy.disneyworld.exception;

public class CharacterAlreadyExistException extends BadRequestException{

    private static final String DESCRIPTION = "Character Already Exist Exception";

    public CharacterAlreadyExistException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
