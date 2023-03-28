package com.example.alkemy.disneyworld.dto;

import lombok.Data;

@Data
public class CharacterDTORequest {
    private String image;
    private String name;
    private Integer age;
    private String story;
}
