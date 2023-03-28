package com.example.alkemy.disneyworld.exception;

public class GenreAlreadyExistException extends BadRequestException{

    private static final String DESCRIPTION = "Genre Already Exist Exception";

    public GenreAlreadyExistException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
