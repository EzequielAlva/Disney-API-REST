package com.example.alkemy.disneyworld.service;

import com.example.alkemy.disneyworld.dto.CharacterDTO;
import com.example.alkemy.disneyworld.dto.CharacterDTOCreation;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterRequest;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterResponse;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.exception.BadRequestException;
import com.example.alkemy.disneyworld.exception.CharacterAlreadyExistException;
import com.example.alkemy.disneyworld.exception.CharacterNotFoundException;
import com.example.alkemy.disneyworld.mapper.MovieCharacterMapper;
import com.example.alkemy.disneyworld.repository.MovieCharacterRepository;
import com.example.alkemy.disneyworld.repository.specification.CharacterSpecification;
import com.example.alkemy.disneyworld.service.implementation.CharacterServiceImpl;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CharacterServiceTest {

    @InjectMocks
    private CharacterServiceImpl characterService;

    @Mock
    private MovieCharacterRepository characterRepository;

    @Mock
    private MovieCharacterMapper characterMapper;

    @Mock
    private CharacterSpecification characterSpecification;

    // -----------------------------------------------------------------
    // ----------------- Create Character ---------------

    @Test
    @DisplayName("Create Character - success")
    void createCharacter1() {
        CharacterDTOCreation characterDTO = generateCharacterDTOCreation();
        given(characterRepository.findByName(any(String.class))).willReturn(Optional.empty());
        given(characterMapper.fromMovieCharacterDTO2Entity(any(CharacterDTOCreation.class))).willReturn(generateCharacterEntity());

        characterService.createCharacter(characterDTO);

        verify(characterRepository, times(1)).findByName(any(String.class));
        verify(characterMapper, times(1)).fromMovieCharacterDTO2Entity(any(CharacterDTOCreation.class));
    }

    @Test
    @DisplayName("Create Character - CharacterAlreadyExistException")
    void createCharacter2() {
        CharacterDTOCreation characterDTO = generateCharacterDTOCreation();
        given(characterRepository.findByName(any(String.class))).willReturn(Optional.of(generateCharacterEntity()));

        assertThrows(CharacterAlreadyExistException.class,
                () -> {
                    CharacterDTOCreation response = characterService.createCharacter(characterDTO);
                    assertNull(response, "Null response");
                },
                "CharacterAlreadyExist exception was not thrown");

        verify(characterRepository, times(1)).findByName(any(String.class));
        verifyNoInteractions(characterMapper);
    }

    // -----------------------------------------------------------------
    // ----------------- Get Character By ID ---------------

    @Test
    @DisplayName("Get Character By ID - success")
    void getCharacterById1() {
        CharacterDTO characterDTO = generateCharacterDTO();
        given(characterRepository.findById(any(String.class))).willReturn(Optional.of(generateCharacterEntity()));
        given(characterMapper.fromCharacterEntity2DTO(any(MovieCharacterEntity.class), any(Boolean.class))).willReturn(characterDTO);

        characterService.getCharacterById(generateId());

        verify(characterRepository, times(1)).findById(anyString());
        verify(characterMapper, times(1)).fromCharacterEntity2DTO(any(MovieCharacterEntity.class), anyBoolean());
    }

    @Test
    @DisplayName("Get Character By ID - CharacterNotFoundException")
    void getCharacterById2() {
        given(characterRepository.findById(any(String.class))).willReturn(Optional.empty());

        assertThrows(CharacterNotFoundException.class,
                () -> {
                    CharacterDTO result = characterService.getCharacterById(generateId());
                    assertNull(result, "Null response");
                },
                "CharacterNotFoundException wasn't thrown");

        verify(characterRepository, times(1)).findById(anyString());
        verifyNoInteractions(characterMapper);
    }

    // -----------------------------------------------------------------
    // ----------------- Update Character ---------------

    @Test
    @DisplayName("Update Character - success")
    void updateCharacter1() {
        Map<Object, Object> objectMap = generateMap();
        CharacterDTO updatedResponse = generateCharacterDTO(
                (String) objectMap.get("image"),
                (String) objectMap.get("name"),
                (Integer) objectMap.get("age"),
                (String) objectMap.get("story"));
        given(characterRepository.findById(anyString())).willReturn(Optional.of(generateCharacterEntity()));
        given(characterMapper.fromCharacterEntity2DTO(any(MovieCharacterEntity.class), anyBoolean())).willReturn(updatedResponse);

        CharacterDTO result = characterService.updateCharacter(generateId(), objectMap);

        verify(characterRepository, times(1)).findById(anyString());
        verify(characterMapper, times(1)).fromCharacterEntity2DTO(any(MovieCharacterEntity.class), anyBoolean());
        assertEquals(updatedResponse, result);
    }

    @Test
    @DisplayName("Update Character - CharacterNotFoundException")
    void updateCharacter2() {
        given(characterRepository.findById(anyString())).willReturn(Optional.empty());

        assertThrows(CharacterNotFoundException.class,
                () -> {
                    CharacterDTO result = characterService.updateCharacter(generateId(), generateMap());
                    assertNull(result, "Null response");
                },
                "CharacterNotFoundException wasn't thrown");


        verify(characterRepository, times(1)).findById(anyString());
        verifyNoInteractions(characterMapper);
    }

    @Test
    @DisplayName("Update Character - NullPointerException")
    void updateCharacter3() {
        Map<Object, Object> objectMap = generateMap();
        CharacterDTO updatedResponse = generateCharacterDTO(
                (String) objectMap.get("image"),
                (String) objectMap.get("name"),
                (Integer) objectMap.get("age"),
                (String) objectMap.get("story"));
        objectMap.put("movies", List.of());

        given(characterRepository.findById(anyString())).willReturn(Optional.of(generateCharacterEntity()));

        assertThrows(NullPointerException.class,
                () -> {
                    CharacterDTO result = characterService.updateCharacter(generateId(), objectMap);
                    assertNull(result, "Null response");
                },
                "NullPointerException wasn't thrown");

        verify(characterRepository, times(1)).findById(anyString());
        verifyNoInteractions(characterMapper);
    }

    @Test
    @DisplayName("Update Character - BadRequestException")
    void updateCharacter4() {
        Map<Object, Object> objectMap = generateMap();
        CharacterDTO updatedResponse = generateCharacterDTO(
                (String) objectMap.get("image"),
                (String) objectMap.get("name"),
                (Integer) objectMap.get("age"),
                (String) objectMap.get("story"));

        objectMap.put("age", "23");

        given(characterRepository.findById(anyString())).willReturn(Optional.of(generateCharacterEntity()));

        assertThrows(BadRequestException.class,
                () -> {
                    CharacterDTO result = characterService.updateCharacter(generateId(), objectMap);
                    assertNull(result, "Null response");
                },
                "BadRequestException wasn't thrown");

        verify(characterRepository, times(1)).findById(anyString());
        verifyNoInteractions(characterMapper);
    }

    // TODO: 29/3/2023 Map with null value

    // -----------------------------------------------------------------
    // ----------------- Filter Characters ---------------
    /*public List<CharacterDTOFilterResponse> findCharactersByFilter(String name, Integer age, Integer weight, String idMovie){
        CharacterDTOFilterRequest characterDTO = new CharacterDTOFilterRequest(name, age, weight, idMovie);
        List<MovieCharacterEntity> filteredCharacters = movieCharacterRepository.findAll(characterSpecification.getByFilters(characterDTO));
        return movieCharacterMapper.fromCharacterFilteredEntityList2DTOList(filteredCharacters);*/

    @Test
    @DisplayName("Filter Characters - success")
    void findCharactersByFilter1() {
        CharacterSpecification specification = new CharacterSpecification();
        Specification<MovieCharacterEntity> characterEntitySpecification = specification.getByFilters(generateDTOFilter());

        String name = "Billy";
        Integer age = 20;
        Integer wight = 80;
        String idMovie = UUID.randomUUID().toString();

        List<MovieCharacterEntity> entityList = generateListOfCharacterEntity();

        doReturn(entityList).when(characterRepository).findAll(any(characterEntitySpecification.getClass()));
        doReturn(characterEntitySpecification).when(characterSpecification).getByFilters(any(CharacterDTOFilterRequest.class));
        when(characterMapper.fromCharacterFilteredEntityList2DTOList(anyList())).thenReturn(generateDTOFilterResponse());

        characterService.findCharactersByFilter(name, age, wight, idMovie);

        verify(characterRepository, times(1)).findAll(characterEntitySpecification);
        verify(characterMapper, times(1)).fromCharacterFilteredEntityList2DTOList(any(List.class));
    }

    @Test
    @DisplayName("Filter Characters - success with null args")
    void findCharactersByFilter2() {
        CharacterSpecification specification = new CharacterSpecification();
        Specification<MovieCharacterEntity> characterEntitySpecification = specification.getByFilters(generateDTOFilter());

        String name = null;
        Integer age = null;
        Integer wight = null;
        String idMovie = null;

        List<MovieCharacterEntity> entityList = generateListOfCharacterEntity();

        doReturn(entityList).when(characterRepository).findAll(any(characterEntitySpecification.getClass()));
        doReturn(characterEntitySpecification).when(characterSpecification).getByFilters(any(CharacterDTOFilterRequest.class));
        when(characterMapper.fromCharacterFilteredEntityList2DTOList(anyList())).thenReturn(generateDTOFilterResponse());

        characterService.findCharactersByFilter(name, age, wight, idMovie);

        verify(characterRepository, times(1)).findAll(characterEntitySpecification);
        verify(characterMapper, times(1)).fromCharacterFilteredEntityList2DTOList(any(List.class));
    }

    static MovieCharacterEntity generateCharacterEntity() {
        return new MovieCharacterEntity(UUID.randomUUID().toString(),
                "./images/char1.jpeg", "Char1",
                20, 78,
                "Character's story",
                Set.of(),
                LocalDate.of(2021, 9, 23),
                LocalDate.of(2021, 9, 23),
                false);
    }

    static List<MovieCharacterEntity> generateListOfCharacterEntity() {
        MovieCharacterEntity char1 = new MovieCharacterEntity(UUID.randomUUID().toString(),
                "./images/Billy.jpeg", "Billy",
                20, 78,
                "Billy's story",
                Set.of(),
                LocalDate.of(2021, 9, 23),
                LocalDate.of(2021, 9, 23),
                false);

        MovieCharacterEntity char2 = new MovieCharacterEntity(UUID.randomUUID().toString(),
                "./images/Joel.jpeg", "Joel",
                30, 86,
                "Joel's story",
                Set.of(),
                LocalDate.of(2018, 6, 22),
                LocalDate.of(2018, 6, 22),
                false);

        MovieCharacterEntity char3 = new MovieCharacterEntity(UUID.randomUUID().toString(),
                "./images/Gery.jpeg", "Gery",
                20, 78,
                "Gery's story",
                Set.of(),
                LocalDate.of(2023, 11, 28),
                LocalDate.of(2023, 11, 28),
                false);

        return List.of(char1, char2, char3);
    }

    static String generateId() {
        return UUID.randomUUID().toString();
    }

    static CharacterDTOCreation generateCharacterDTOCreation() {
        return CharacterDTOCreation.builder()
                .image("./images/Gery.jpeg")
                .name("Gery")
                .age(23)
                .story("Gery's story")
                .build();
    }

    static CharacterDTO generateCharacterDTO() {
        return CharacterDTO.builder()
                .image("./images/Gery.jpeg")
                .name("Gery")
                .age(23)
                .story("Gery's story")
                .movies(List.of())
                .build();
    }

    static CharacterDTO generateCharacterDTO(String image, String name, Integer age, String story) {
        return CharacterDTO.builder()
                .image(image)
                .name(name)
                .age(age)
                .story(story)
                .build();
    }

    static Map<Object, Object> generateMap() {
        Map<Object, Object> myCharacter = new HashMap<>();
        myCharacter.put("image", "./images/Brian.jpeg");
        myCharacter.put("name", "Brian");
        myCharacter.put("age", 23);
        myCharacter.put("story", "Brian's story");

        return myCharacter;
    }

    static CharacterDTOFilterRequest generateDTOFilter() {
        return CharacterDTOFilterRequest.builder()
                .idMovie(UUID.randomUUID().toString())
                .name("Billy")
                .weight(80)
                .age(20)
                .build();
    }

    static List<CharacterDTOFilterResponse> generateDTOFilterResponse() {
        CharacterDTOFilterResponse filter1 = CharacterDTOFilterResponse.builder().image("./images/Billy.jpeg").name("Billy").build();
        CharacterDTOFilterResponse filter2 = CharacterDTOFilterResponse.builder().image("./images/Ezequiel.jpeg").name("Ezequiel").build();
        CharacterDTOFilterResponse filter3 = CharacterDTOFilterResponse.builder().image("./images/Valentina.jpeg").name("Valentina").build();
        return List.of(filter1, filter2, filter3);
    }
}
