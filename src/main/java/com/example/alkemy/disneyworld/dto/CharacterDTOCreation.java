package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterDTOCreation {
    private String image;
    private String name;
    private Integer age;
    private String story;
}
