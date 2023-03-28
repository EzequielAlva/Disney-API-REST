package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.dto.MovieDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.MovieDTORequest;
import com.example.alkemy.disneyworld.dto.MovieDTOResponse;
import com.example.alkemy.disneyworld.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/disney")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping(value = "/movie")
    public ResponseEntity<MovieDTOResponse> createMovie(@RequestBody MovieDTORequest movieDTORequest){
        MovieDTOResponse response = movieService.createMovie(movieDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/movie/{id}")
    public ResponseEntity<MovieDTOResponse> getMovieByID(@PathVariable String id){
        MovieDTOResponse response = movieService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/movie/{id}")
    public ResponseEntity<String> deleteMovieById(@PathVariable String id){
        movieService.deleteMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Movie has been deleted correctly");
    }

    @PatchMapping(value = "/movie/{id}")
    public ResponseEntity<MovieDTOResponse> updateMovie(@PathVariable String id, @RequestBody Map<Object, Object> movieDTORequest){
        MovieDTOResponse response = movieService.updateMovie(id, movieDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/all-movies")
    public ResponseEntity<List<MovieDTOResponse>> getAllMovies(){
        List<MovieDTOResponse> response = movieService.getAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/movies")
    public ResponseEntity<List<MovieDTOFilterResponse>> getMoviesByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "genre") String genreId,
            @RequestParam(required = false, defaultValue = "ASC") String order
    ){
        List<MovieDTOFilterResponse> response = movieService.getMoviesByFilters(name, genreId, order);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/movies/{idMovie}/characters/{idCharacter}")
    public ResponseEntity<String> addCharacterToMovie(@PathVariable String idMovie, @PathVariable String idCharacter){
        movieService.addCharacterToMovie(idMovie, idCharacter);
        return ResponseEntity.status(HttpStatus.OK).body("Character added correctly");
    }

    @DeleteMapping(value = "/movies/{idMovie}/characters/{idCharacter}")
    public ResponseEntity<String> removeCharacterFromMovie(@PathVariable String idMovie, @PathVariable String idCharacter){
        movieService.removeCharacterFromMovie(idMovie, idCharacter);
        return ResponseEntity.status(HttpStatus.OK).body("Character removed correctly");
    }
}
