package com.example.alkemy.disneyworld.service;

import com.example.alkemy.disneyworld.dto.CharacterDTO;
import com.example.alkemy.disneyworld.dto.CharacterDTOCreation;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterResponse;

import java.util.List;
import java.util.Map;

public interface CharacterService {
    CharacterDTOCreation createCharacter(CharacterDTOCreation characterDTOCreation);
    CharacterDTO getCharacterById(String id);
    void deleteById(String id);
    CharacterDTO updateCharacter(String id, Map<Object, Object> bodyMap);
    List<CharacterDTOFilterResponse> findCharactersByFilter(String name, Integer age, Integer weight, String idMovie);
}
