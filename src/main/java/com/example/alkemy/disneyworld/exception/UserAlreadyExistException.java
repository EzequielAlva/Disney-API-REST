package com.example.alkemy.disneyworld.exception;

public class UserAlreadyExistException extends BadRequestException{

    private static final String DESCRIPTION = "User Already Exist Exception";

    public UserAlreadyExistException(String details) {
        super(DESCRIPTION + ". " + details);
    }
}
