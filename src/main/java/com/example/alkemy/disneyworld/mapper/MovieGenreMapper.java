package com.example.alkemy.disneyworld.mapper;

import com.example.alkemy.disneyworld.dto.GenreDTO;
import com.example.alkemy.disneyworld.dto.GenreDTOResponse;
import com.example.alkemy.disneyworld.dto.MovieDTORequest;
import com.example.alkemy.disneyworld.entity.MovieEntity;
import com.example.alkemy.disneyworld.entity.MovieGenreEntity;
import com.example.alkemy.disneyworld.repository.MovieGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MovieGenreMapper {

    private MovieMapper movieMapper;
    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Autowired
    public MovieGenreMapper(@Lazy MovieMapper movieMapper){
        this.movieMapper = movieMapper;
    }

    //  --------------------- CREATE FROM DTO TO ENTITY ---------------------
    public MovieGenreEntity fromGenreDTO2GenreEntity(GenreDTO dto){
        Optional<MovieGenreEntity> optional = movieGenreRepository.findByName(dto.getName());
        if(optional.isEmpty()){
            MovieGenreEntity entity = new MovieGenreEntity();
            entity.setImage(dto.getImage());
            entity.setName(dto.getName());
            return entity;
        }
        return optional.get();
    }

    public Set<MovieGenreEntity> fromGenreDTOList2GenreEntitySet(List<GenreDTO> dtos){
        Set<MovieGenreEntity> entities = new HashSet<>();
        for(GenreDTO dto : dtos){
            entities.add(this.fromGenreDTO2GenreEntity(dto));
        }
        return entities;
    }

    //  --------------------- FROM ENTITY TO DTO    ---------------------
    public GenreDTOResponse fromGenreEntity2GenreDTO(MovieGenreEntity entity){
        GenreDTOResponse dto = new GenreDTOResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        return dto;
    }

    public List<GenreDTOResponse> fromGenreEntitySet2GenreDTOList(Set<MovieGenreEntity> entities){
        List<GenreDTOResponse> dtos = new ArrayList<>();
        for(MovieGenreEntity entity : entities){
            dtos.add(this.fromGenreEntity2GenreDTO(entity));
        }
        return dtos;
    }
}
