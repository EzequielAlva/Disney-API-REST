package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterDTO {
    private String image;
    private String name;
    private Integer age;
    private String story;
    private List<MovieDTOResponse> movies;
}
