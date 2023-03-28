package com.example.alkemy.disneyworld.exception;

public class UserNotFoundException extends NotFoundException{

    private static final String DESCRIPTION = "User Not Found Exception";

    public UserNotFoundException(String details) {
        super(DESCRIPTION + ". " + details);
    }
}
