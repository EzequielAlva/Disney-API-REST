package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.dto.GenreDTO;
import com.example.alkemy.disneyworld.dto.GenreDTOResponse;
import com.example.alkemy.disneyworld.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/disney")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping(value = "/genre")
    public ResponseEntity<GenreDTOResponse> createGenre(@RequestBody GenreDTO genreDTO){
        GenreDTOResponse response = genreService.createGenre(genreDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
