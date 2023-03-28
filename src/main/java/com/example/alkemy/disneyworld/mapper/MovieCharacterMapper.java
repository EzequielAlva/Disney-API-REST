package com.example.alkemy.disneyworld.mapper;

import com.example.alkemy.disneyworld.dto.*;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.repository.MovieCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MovieCharacterMapper {
    @Autowired
    private MovieCharacterRepository movieCharacterRepository;

    private MovieMapper movieMapper;
    @Autowired
    public MovieCharacterMapper(@Lazy MovieMapper movieMapper){
        this.movieMapper = movieMapper;
    }

    //  --------------------- CREATE FROM DTO TO ENTITY ---------------------
    public MovieCharacterEntity fromMovieCharacterDTO2Entity(CharacterDTOCreation dto){
        Optional<MovieCharacterEntity> optional = movieCharacterRepository.findByName(dto.getName());
        if(optional.isEmpty()){
            MovieCharacterEntity entity = new MovieCharacterEntity();
            entity.setImage(dto.getImage());
            entity.setName(dto.getName());
            entity.setAge(dto.getAge());
            entity.setStory(dto.getStory());
            return entity;
        }
        return optional.get();
    }

    public Set<MovieCharacterEntity> fromCharacterDTOList2EntitySet(List<CharacterDTOCreation> dtos){
        Set<MovieCharacterEntity> entities = new HashSet<>();
        for(CharacterDTOCreation dto : dtos){
            entities.add(this.fromMovieCharacterDTO2Entity(dto));
        }
        return entities;
    }

    //  ---------------------    FROM ENTITY TO DTO    ---------------------
    public CharacterDTOCreation fromCharacterEntity2DTO(MovieCharacterEntity entity){
        CharacterDTOCreation dto = new CharacterDTOCreation();
        dto.setImage(entity.getImage());
        dto.setName(entity.getName());
        dto.setAge(entity.getAge());
        dto.setStory(entity.getStory());
        return dto;
    }

    public CharacterDTO fromCharacterEntity2DTO(MovieCharacterEntity entity, boolean access){
        CharacterDTO dto = new CharacterDTO();
        dto.setImage(entity.getImage());
        dto.setName(entity.getName());
        dto.setAge(entity.getAge());
        dto.setStory(entity.getStory());
        if(access){
            List<MovieDTOResponse> movies = movieMapper.fromMovieEntitySet2DTOList(entity.getMovies(), false);
            dto.setMovies(movies);
        }
        return dto;
    }

    public List<CharacterDTOCreation> fromCharacterEntitySet2DtoList(Set<MovieCharacterEntity> entities){
        List<CharacterDTOCreation> dtos = new ArrayList<>();
        for(MovieCharacterEntity entity : entities){
            dtos.add(this.fromCharacterEntity2DTO(entity));
        }
        return dtos;
    }

    //  ---------------------    FROM FILTERED ENTITY TO DTO    ---------------------
    public CharacterDTOFilterResponse fromCharacterFilteredEntity2DTO(MovieCharacterEntity entity){
        return new CharacterDTOFilterResponse(entity.getImage(), entity.getName());
    }

    public List<CharacterDTOFilterResponse> fromCharacterFilteredEntityList2DTOList(List<MovieCharacterEntity> entities){
        List<CharacterDTOFilterResponse> dtos = new ArrayList<>();
        entities.forEach(entity -> {
            dtos.add(this.fromCharacterFilteredEntity2DTO(entity));
        });
        return dtos;
    }
}
