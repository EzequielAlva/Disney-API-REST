package com.example.alkemy.disneyworld.dto;

import lombok.Data;

import java.util.List;

@Data
public class CharacterDTO {
    private String image;
    private String name;
    private Integer age;
    private String story;
    private List<MovieDTOResponse> movies;
}
