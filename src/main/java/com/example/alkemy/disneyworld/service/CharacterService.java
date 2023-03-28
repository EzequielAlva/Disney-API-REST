package com.example.alkemy.disneyworld.service;

import com.example.alkemy.disneyworld.dto.CharacterDTO;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.CharacterDTORequest;
import com.example.alkemy.disneyworld.dto.CharacterDTOResponse;

import java.util.List;
import java.util.Map;

public interface CharacterService {
    CharacterDTOResponse createCharacter(CharacterDTORequest characterDTORequest);
    CharacterDTO getCharacterById(String id);
    void deleteById(String id);
    CharacterDTO updateCharacter(String id, Map<Object, Object> bodyMap);
    List<CharacterDTOFilterResponse> findCharactersByFilter(String name, Integer age, Integer weight, String idMovie);
}
