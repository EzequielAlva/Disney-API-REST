package com.example.alkemy.disneyworld.exception;

public class MalformedHeaderExeption extends BadRequestException{

    private static final String DESCRIPTION = "Token with wrong format";

    MalformedHeaderExeption(String details){
        super(DESCRIPTION + ". " + details);
    }
}
