package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.dto.CharacterDTO;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.CharacterDTORequest;
import com.example.alkemy.disneyworld.dto.CharacterDTOResponse;
import com.example.alkemy.disneyworld.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/disney")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @PostMapping(value = "/character")
    public ResponseEntity<CharacterDTOResponse> createCharacter(@RequestBody CharacterDTORequest characterDTORequest){
        CharacterDTOResponse response = characterService.createCharacter(characterDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/character/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable String id){
        CharacterDTO response = characterService.getCharacterById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/character/{id}")
    public ResponseEntity<String> deleteCharacterById(@PathVariable String id){
        characterService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Character has been deleted correctly");
    }

    @PatchMapping(value = "/character/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable String id, @RequestBody Map<Object, Object> bodyMap){
        CharacterDTO response = characterService.updateCharacter(id, bodyMap);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/characters")
    public ResponseEntity<List<CharacterDTOFilterResponse>> findCharactersByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Integer weight,
            @RequestParam(required = false, name = "movie") String idMovie
    ){
        List<CharacterDTOFilterResponse> response = characterService.findCharactersByFilter(name, age, weight, idMovie);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
