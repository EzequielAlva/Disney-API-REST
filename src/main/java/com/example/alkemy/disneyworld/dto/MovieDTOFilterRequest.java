package com.example.alkemy.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTOFilterRequest {
    private String name;
    private String idGenre;
    private String order;

    public boolean isASC(){return this.order.compareToIgnoreCase("ASC") == 0;}
}
