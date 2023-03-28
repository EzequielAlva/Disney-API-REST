package com.example.alkemy.disneyworld.service;

import com.example.alkemy.disneyworld.dto.GenreDTO;
import com.example.alkemy.disneyworld.dto.GenreDTOResponse;

public interface GenreService {
    GenreDTOResponse createGenre(GenreDTO genreDTO);
}
