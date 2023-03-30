package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterDTOFilterRequest {
    private String name;
    private Integer age;
    private Integer weight;
    private String idMovie;
}
