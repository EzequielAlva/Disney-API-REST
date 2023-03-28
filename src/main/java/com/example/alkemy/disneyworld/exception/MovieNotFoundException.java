package com.example.alkemy.disneyworld.exception;

public class MovieNotFoundException extends NotFoundException {

    private static final String DESCRIPTION = "Movie Not Found";

    public MovieNotFoundException(String details){
        super(DESCRIPTION + ". " + details);
    }
}
