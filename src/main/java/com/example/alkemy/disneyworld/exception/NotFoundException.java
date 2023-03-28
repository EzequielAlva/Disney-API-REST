package com.example.alkemy.disneyworld.exception;

public class NotFoundException extends RuntimeException{

    private static final String DESCRIPTION = "Not Found Exception (404)";

    public NotFoundException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
