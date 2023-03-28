package com.example.alkemy.disneyworld.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTORequest {
    private String image;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate creationDate;
    private Integer score;
    private List<GenreDTO> genres;
    private List<CharacterDTORequest> characters;
}
