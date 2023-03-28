package com.example.alkemy.disneyworld.service.implementation;

import com.example.alkemy.disneyworld.dto.GenreDTO;
import com.example.alkemy.disneyworld.dto.GenreDTOResponse;
import com.example.alkemy.disneyworld.entity.MovieGenreEntity;
import com.example.alkemy.disneyworld.mapper.MovieGenreMapper;
import com.example.alkemy.disneyworld.repository.MovieGenreRepository;
import com.example.alkemy.disneyworld.exception.GenreAlreadyExistException;
import com.example.alkemy.disneyworld.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private static final String ALREADY_EXIST_MESSAGE = "This genre already exist in database";
    @Autowired
    private MovieGenreRepository movieGenreRepository;
    @Autowired
    private MovieGenreMapper movieGenreMapper;

    public GenreDTOResponse createGenre(GenreDTO genreDTO){
        Optional<MovieGenreEntity> genre = movieGenreRepository.findByName(genreDTO.getName());
        if(!genre.isEmpty()){
            throw new GenreAlreadyExistException(ALREADY_EXIST_MESSAGE);
        }
        MovieGenreEntity saveGenre = movieGenreMapper.fromGenreDTO2GenreEntity(genreDTO);
        movieGenreRepository.save(saveGenre);
        return movieGenreMapper.fromGenreEntity2GenreDTO(saveGenre);
    }
}
