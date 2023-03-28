package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTOFilterResponse {
    private String image;
    private String title;
    private LocalDate creationDate;
}
