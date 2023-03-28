package com.example.alkemy.disneyworld.exception;

public class ForbiddenException extends RuntimeException{

    private static final String DESCRIPTION = "Forbidden Exception (403)";

    public ForbiddenException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
