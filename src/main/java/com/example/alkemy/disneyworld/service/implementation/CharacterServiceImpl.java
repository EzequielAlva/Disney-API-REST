package com.example.alkemy.disneyworld.service.implementation;

import com.example.alkemy.disneyworld.dto.*;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.mapper.MovieCharacterMapper;
import com.example.alkemy.disneyworld.service.CharacterService;
import com.example.alkemy.disneyworld.repository.MovieCharacterRepository;
import com.example.alkemy.disneyworld.repository.specification.CharacterSpecification;
import com.example.alkemy.disneyworld.exception.BadRequestException;
import com.example.alkemy.disneyworld.exception.CharacterAlreadyExistException;
import com.example.alkemy.disneyworld.exception.CharacterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {
    private static final String NOT_FOUND_MESSAGE = "This character does not exist in database";
    private static final String ALREADY_EXIST_MESSAGE = "This character already exist in database";
    private static final String BAD_REQUEST_FIELD_MESSAGE = "The following field is not allowed to update: ";
    private static final String BAD_REQUEST_TYPE_MISMATCH_MESSAGE = "The value of the fields does not match: ";
    @Autowired
    private MovieCharacterRepository movieCharacterRepository;
    @Autowired
    private CharacterSpecification characterSpecification;
    @Autowired
    private MovieCharacterMapper movieCharacterMapper;

    // -------------- CREATE CHARACTER --------------
    public CharacterDTOCreation createCharacter(CharacterDTOCreation characterDTOCreation){
        Optional<MovieCharacterEntity> optionalCharacter = movieCharacterRepository.findByName(characterDTOCreation.getName());
        if(!optionalCharacter.isEmpty()){
            throw new CharacterAlreadyExistException(ALREADY_EXIST_MESSAGE);
        }
        MovieCharacterEntity character = movieCharacterMapper.fromMovieCharacterDTO2Entity(characterDTOCreation);
        movieCharacterRepository.save(character);
        return movieCharacterMapper.fromCharacterEntity2DTO(character);
    }

    // -------------- GET CHARACTER BY ID --------------
    public CharacterDTO getCharacterById(String id){
        Optional<MovieCharacterEntity> character = movieCharacterRepository.findById(id);
        if(character.isEmpty()){
            throw new CharacterNotFoundException(NOT_FOUND_MESSAGE);
        }
        return movieCharacterMapper.fromCharacterEntity2DTO(character.get(), true);
    }

    // -------------- DELETE CHARACTER --------------
    public void deleteById(String id){
        Optional<MovieCharacterEntity> character = movieCharacterRepository.findById(id);
        if(character.isEmpty()){
            throw new CharacterNotFoundException(NOT_FOUND_MESSAGE);
        }
        movieCharacterRepository.deleteById(id);
    }

    // -------------- UPDATE CHARACTER --------------
    public CharacterDTO updateCharacter(String id, Map<Object, Object> bodyMap){
        Optional<MovieCharacterEntity> character = movieCharacterRepository.findById(id);
        if(character.isEmpty()){
            throw new CharacterNotFoundException(NOT_FOUND_MESSAGE);
        }
        movieCharacterRepository.save(changeValues(character.get(), bodyMap));
        return movieCharacterMapper.fromCharacterEntity2DTO(character.get(), true);
    }

    private MovieCharacterEntity changeValues(MovieCharacterEntity myCharacter, Map<Object, Object> bodyMap){
        bodyMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(MovieCharacterEntity.class, (String) key);
            if(field == null || field.getName().equals("movies")){
                throw new NullPointerException(BAD_REQUEST_FIELD_MESSAGE + (String) key);
            }
            if(field.getType().getName() != value.getClass().getName()){
                throw new BadRequestException(BAD_REQUEST_TYPE_MISMATCH_MESSAGE + "Should be " + field.getType().getName() + " but is " + value.getClass().getName());
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, myCharacter, value);
        });
        return myCharacter;
    }

    // -------------- FILTER CHARACTERS --------------
    public List<CharacterDTOFilterResponse> findCharactersByFilter(String name, Integer age, Integer weight, String idMovie){
        CharacterDTOFilterRequest characterDTO = new CharacterDTOFilterRequest(name, age, weight, idMovie);
        List<MovieCharacterEntity> filteredCharacters = movieCharacterRepository.findAll(characterSpecification.getByFilters(characterDTO));
        return movieCharacterMapper.fromCharacterFilteredEntityList2DTOList(filteredCharacters);
    }
}
