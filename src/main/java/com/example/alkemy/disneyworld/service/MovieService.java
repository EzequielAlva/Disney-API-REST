package com.example.alkemy.disneyworld.service;


import com.example.alkemy.disneyworld.dto.MovieDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.MovieDTORequest;
import com.example.alkemy.disneyworld.dto.MovieDTOResponse;
import com.example.alkemy.disneyworld.dto.MovieUpdateDTORequest;

import java.util.List;
import java.util.Map;

public interface MovieService {
    MovieDTOResponse createMovie(MovieDTORequest movieDTORequest);
    MovieDTOResponse getMovieById(String id);
    void deleteMovieById(String id);
    MovieDTOResponse updateMovie(String id, Map<Object, Object> movieDTORequest);
    List<MovieDTOResponse> getAllMovies();
    List<MovieDTOFilterResponse> getMoviesByFilters(String name, String genreId, String order);
    void addCharacterToMovie(String idMovie, String idCharacter);
    void removeCharacterFromMovie(String idMovie, String idCharacter);
}
