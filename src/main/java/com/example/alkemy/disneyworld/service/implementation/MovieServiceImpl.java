package com.example.alkemy.disneyworld.service.implementation;

import com.example.alkemy.disneyworld.dto.MovieDTOFilterRequest;
import com.example.alkemy.disneyworld.dto.MovieDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.MovieDTORequest;
import com.example.alkemy.disneyworld.dto.MovieDTOResponse;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.entity.MovieEntity;
import com.example.alkemy.disneyworld.mapper.MovieGenreMapper;
import com.example.alkemy.disneyworld.mapper.MovieMapper;
import com.example.alkemy.disneyworld.service.MovieService;
import com.example.alkemy.disneyworld.repository.MovieCharacterRepository;
import com.example.alkemy.disneyworld.repository.MovieRepository;
import com.example.alkemy.disneyworld.repository.specification.MovieSpecification;
import com.example.alkemy.disneyworld.exception.BadRequestException;
import com.example.alkemy.disneyworld.exception.CharacterAlreadyExistException;
import com.example.alkemy.disneyworld.exception.CharacterNotFoundException;
import com.example.alkemy.disneyworld.exception.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private static final String NOT_FOUND_MESSAGE = "This movie does not exist in database";
    private static final String BAD_REQUEST_FIELD_MESSAGE = "The following field is not allowed to update: ";
    private static final String BAD_REQUEST_TYPE_MISMATCH_MESSAGE = "The value of the fields does not match: ";
    private static final String BAD_REQUEST_SCORE_MESSAGE = "The score must be between 0 and 5";
    private static final String CHARACTER_NOT_FOUND_MESSAGE = "The character does not exist in database";
    private static final String CHARACTER_ALREADY_EXIST_MESSAGE = "This character already exist in this movie";
    private static final String BAD_REQUEST_NULL_FIELDS_MESSAGE = "List of characters or genres must not be null, at least an empty list";
    private static final String CHARACTER_DOES_NOT_EXIST_MESSAGE = "This character does not exist in this movie";
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieSpecification movieSpecification;
    @Autowired
    private MovieCharacterRepository movieCharacterRepository;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private MovieGenreMapper movieGenreMapper;

    // -------------- CREATE MOVIE --------------
    public MovieDTOResponse createMovie(MovieDTORequest movieDTO){
        if(movieDTO.getScore() > 5 || movieDTO.getScore() < 0 ){
            throw new BadRequestException(BAD_REQUEST_SCORE_MESSAGE);
        }
        if(movieDTO.getCharacters() == null || movieDTO.getGenres() == null){
            throw new BadRequestException(BAD_REQUEST_NULL_FIELDS_MESSAGE);
        }
        MovieEntity movie = movieMapper.fromMovieDTO2MovieEntity(movieDTO);
        MovieEntity savedMovie = movieRepository.save(movie);
        return movieMapper.fromMovieEntity2MovieDTO(savedMovie, true);
    }

    // -------------- GET MOVIE BY ID --------------
    public MovieDTOResponse getMovieById(String id){
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new MovieNotFoundException(NOT_FOUND_MESSAGE);
        }
        return movieMapper.fromMovieEntity2MovieDTO(movie.get(), true);
    }

    // -------------- DELETE MOVIE --------------
    public void deleteMovieById(String id){
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new MovieNotFoundException(NOT_FOUND_MESSAGE);
        }
        movieRepository.deleteById(id);
    }

    // -------------- UPDATE MOVIE --------------
    public MovieDTOResponse updateMovie(String id, Map<Object, Object> movieDTORequest){
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new MovieNotFoundException(NOT_FOUND_MESSAGE);
        }
        movieRepository.save(changeValues(movie.get(), movieDTORequest));
        MovieDTOResponse response = movieMapper.fromMovieEntity2MovieDTO(movie.get(), true);
        return response;
    }

    private MovieEntity changeValues(MovieEntity myMovie, Map<Object, Object> dto){
        dto.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(MovieEntity.class, (String) key);

            if(field == null || field.getName().equals("genres") || field.getName().equals("characters")){
                throw new NullPointerException(BAD_REQUEST_FIELD_MESSAGE + (String) key);
            }
            if(field.getType().getName() != value.getClass().getName()){
                throw new BadRequestException(BAD_REQUEST_TYPE_MISMATCH_MESSAGE + "Should be " + field.getType().getName() + " but is " + value.getClass().getName());
            }
            field.setAccessible(true);

            if(field.getName().equals("creationDate")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String date = (String) value;
                LocalDate localDate = LocalDate.parse(date, formatter);
                ReflectionUtils.setField(field, myMovie, localDate);
            } else {
                ReflectionUtils.setField(field, myMovie, value);
            }

        });
        return myMovie;
    }

    // -------------- GET ALL MOVIES --------------
    public List<MovieDTOResponse> getAllMovies(){
        Set<MovieEntity> movies = movieRepository.findAll().stream().collect(Collectors.toSet());
        return movieMapper.fromMovieEntitySet2DTOList(movies, true);
    }

    // -------------- GET ALL MOVIES BY FILTER --------------

    public List<MovieDTOFilterResponse> getMoviesByFilters(String name, String genreId, String order){
        if(!Objects.equals(order, "ASC") && !Objects.equals(order, "DESC")){
            throw new BadRequestException("order argument not accepted, possible values are: ASC (default) or DESC");
        }
        MovieDTOFilterRequest movieDTO = new MovieDTOFilterRequest(name, genreId, order);
        List<MovieEntity> filteredMovies = movieRepository.findAll(movieSpecification.getByFilter(movieDTO));
        return movieMapper.fromMovieEntityFilterList2DTOList(filteredMovies);
    }

    // -------------- ADD/REMOVE CHARACTER TO MOVIE --------------

    public void addCharacterToMovie(String idMovie, String idCharacter){
        Optional<MovieEntity> movie = movieRepository.findById(idMovie);
        Optional<MovieCharacterEntity> character = movieCharacterRepository.findById(idCharacter);
        if(movie.isEmpty()){
            throw new MovieNotFoundException(NOT_FOUND_MESSAGE);
        } else if (character.isEmpty()) {
            throw new CharacterNotFoundException(CHARACTER_NOT_FOUND_MESSAGE);
        } else if(movie.get().getCharacters().contains(character.get())){
            throw new CharacterAlreadyExistException(CHARACTER_ALREADY_EXIST_MESSAGE);
        }
        movie.get().addCharacter(character.get());

        movieRepository.save(movie.get());
    }

    public void removeCharacterFromMovie(String idMovie, String idCharacter){
        Optional<MovieEntity> movie = movieRepository.findById(idMovie);
        Optional<MovieCharacterEntity> character = movieCharacterRepository.findById(idCharacter);
        if(movie.isEmpty()){
            throw new MovieNotFoundException(NOT_FOUND_MESSAGE);
        } else if (character.isEmpty()) {
            throw new CharacterNotFoundException(CHARACTER_NOT_FOUND_MESSAGE);
        } else if (!movie.get().getCharacters().contains(character.get())) {
            throw new CharacterNotFoundException(CHARACTER_DOES_NOT_EXIST_MESSAGE);
        }
        movie.get().removeCharacter(character.get());
        movieRepository.save(movie.get());
    }
}
