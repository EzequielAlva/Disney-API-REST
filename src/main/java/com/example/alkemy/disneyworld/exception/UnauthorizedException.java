package com.example.alkemy.disneyworld.exception;

public class UnauthorizedException extends RuntimeException{

    private static final String DESCRIPTION = "Unauthorized Exception (401)";

    public UnauthorizedException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
