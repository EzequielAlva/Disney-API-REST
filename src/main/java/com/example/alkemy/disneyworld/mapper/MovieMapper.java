package com.example.alkemy.disneyworld.mapper;

import com.example.alkemy.disneyworld.dto.*;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.entity.MovieEntity;
import com.example.alkemy.disneyworld.entity.MovieGenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MovieMapper {
    @Autowired
    private MovieGenreMapper movieGenreMapper;
    @Autowired
    private MovieCharacterMapper movieCharacterMapper;

    //  --------------------- CREATE FROM DTO TO ENTITY ---------------------
    public MovieEntity fromMovieDTO2MovieEntity(MovieDTORequest dto){
        MovieEntity entity = new MovieEntity();
        entity.setImage(dto.getImage());
        entity.setTitle(dto.getTitle());
        entity.setCreationDate(dto.getCreationDate());
        entity.setScore(dto.getScore());
        Set<MovieGenreEntity> genres = movieGenreMapper.fromGenreDTOList2GenreEntitySet(dto.getGenres());
        entity.setGenres(genres);
        Set<MovieCharacterEntity> characters = movieCharacterMapper.fromCharacterDTOList2EntitySet(dto.getCharacters());
        entity.setCharacters(characters);
        return entity;
    }

    public Set<MovieEntity> fromMovieDTOList2EntitySet(List<MovieDTORequest> dtos){
        Set<MovieEntity> entities = new HashSet<>();
        dtos.forEach(dto -> entities.add(this.fromMovieDTO2MovieEntity(dto)));
        return entities;
    }

    //  --------------------- CREATE FROM ENTITY TO DTO    ---------------------
    public MovieDTOResponse fromMovieEntity2MovieDTO(MovieEntity entity, boolean access){
        MovieDTOResponse dto = new MovieDTOResponse();
        dto.setId(entity.getId());
        dto.setImage(entity.getImage());
        dto.setTitle(entity.getTitle());
        dto.setCreationDate(entity.getCreationDate());
        dto.setScore(entity.getScore());
        List<GenreDTOResponse> genres = movieGenreMapper.fromGenreEntitySet2GenreDTOList(entity.getGenres());
        dto.setGenres(genres);
        if(access){
            List<CharacterDTOResponse> characters = movieCharacterMapper.fromCharacterEntitySet2DtoList(entity.getCharacters());
            dto.setCharacters(characters);
        }

        return dto;
    }

    public List<MovieDTOResponse> fromMovieEntitySet2DTOList(Set<MovieEntity> entities, boolean access){
        List<MovieDTOResponse> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(this.fromMovieEntity2MovieDTO(entity, access)));
        return dtos;
    }

    public MovieDTOFilterResponse fromMovieEntityFilter2DTO(MovieEntity entity){
        MovieDTOFilterResponse dto = new MovieDTOFilterResponse(entity.getImage(), entity.getTitle(), entity.getCreationDate());
        return dto;
    }

    public List<MovieDTOFilterResponse> fromMovieEntityFilterList2DTOList(List<MovieEntity> entities){
        List<MovieDTOFilterResponse> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(this.fromMovieEntityFilter2DTO(entity)));
        return dtos;
    }

}
