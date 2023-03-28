package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTOFilterRequest {
    private String name;
    private Integer age;
    private Integer weight;
    private String idMovie;
}
