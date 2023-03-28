package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.auth.config.ApplicationConfig;
import com.example.alkemy.disneyworld.auth.config.SecurityConfiguration;
import com.example.alkemy.disneyworld.auth.jwt.JwtConfig;
import com.example.alkemy.disneyworld.auth.jwt.JwtService;
import com.example.alkemy.disneyworld.auth.user.UserServiceDetailsImpl;
import com.example.alkemy.disneyworld.dto.CharacterDTO;
import com.example.alkemy.disneyworld.dto.CharacterDTOCreation;
import com.example.alkemy.disneyworld.dto.CharacterDTOFilterResponse;
import com.example.alkemy.disneyworld.service.CharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(CharacterController.class)
@Import({SecurityConfiguration.class, ApplicationConfig.class})
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserServiceDetailsImpl userServiceDetails;

    // ---------------------------------------------------------------------------
    // -------------------------- Create Character --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Create Character - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCharacter1() throws Exception {
        CharacterDTOCreation character = generateDTORequest();
        when(characterService.createCharacter(any(CharacterDTOCreation.class))).thenReturn(character);

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/character")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(character)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).createCharacter(any(CharacterDTOCreation.class));
    }

    @Test
    @DisplayName("Create Character - Http Status 403 - Role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void createCharacter2() throws Exception {
        CharacterDTOCreation character = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/character")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Create Character - Http Status 403 - User without roles not allowed")
    void createCharacter3() throws Exception {
        CharacterDTOCreation character = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/character")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Create Character - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCharacter4() throws Exception {
        CharacterDTOCreation character = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/chaaracter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Create Character - Http Status 400 - null Body")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCharacter5() throws Exception {
        CharacterDTOCreation character = null;

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/character")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Get Character By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Get Character By ID - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getCharacterById1() throws Exception {
        CharacterDTO character = generateCharacterDTO();
        when(characterService.getCharacterById(any(String.class))).thenReturn(character);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/character/{id}", generateID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(character)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).getCharacterById(any(String.class));
    }

    @Test
    @DisplayName("Get Character By ID - Http Status 200 - USER OK")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void getCharacterById2() throws Exception {
        CharacterDTO character = generateCharacterDTO();
        when(characterService.getCharacterById(any(String.class))).thenReturn(character);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/character/{id}", generateID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(character)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).getCharacterById(any(String.class));
    }

    @Test
    @DisplayName("Get Character By ID - Http Status 403 - User without roles not allowed")
    void getCharacterById3() throws Exception {
        CharacterDTO character = generateCharacterDTO();

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/character/{id}", generateID()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Get Character By ID - Http Status 404 - No id")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getCharacterById4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disney/character/{id}", ""))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Delete Character By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Delete Character By ID - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCharacterById1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/character/{id}", generateID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Character has been deleted correctly"))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).deleteById(any(String.class));
    }

    @Test
    @DisplayName("Delete Character By ID - Http Status 403 - Role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void deleteCharacterById2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/character/{id}", generateID()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Delete Character By ID - Http Status 403 - User without roles not allowed")
    void deleteCharacterById3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/character/{id}", generateID()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Delete Character By ID - Http Status 404 - No id")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCharacterById4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/character/{id}", ""))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Update Character By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Update Character By ID - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCharacter1() throws Exception {
        CharacterDTO character = generateCharacterDTO();
        when(characterService.updateCharacter(any(String.class), any(HashMap.class))).thenReturn(character);

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/character/{id}", generateID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(character)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).updateCharacter(any(String.class), any(HashMap.class));
    }

    @Test
    @DisplayName("Update Character By ID - Http Status 403 - Role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void updateCharacter2() throws Exception {
        CharacterDTO character = generateCharacterDTO();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/character/{id}", generateID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Update Character By ID - Http Status 403 - User without roles not allowed")
    void updateCharacter3() throws Exception {
        CharacterDTO character = generateCharacterDTO();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/character/{id}", generateID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Update Character By ID - Http Status 404 - No id")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCharacter4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/character/{id}", ""))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Find Characters By Filters --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Find Characters By Filters - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void findCharactersByFilter1() throws Exception {
        List<CharacterDTOFilterResponse> response = List.of(generateFilterResponse());
        when(characterService.findCharactersByFilter(any(String.class), any(Integer.class), any(Integer.class), any(String.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/characters")
                        .param("name", "Billy")
                        .param("age", String.valueOf(28))
                        .param("weight", String.valueOf(80))
                        .param("movie", generateID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).findCharactersByFilter(any(String.class),
                any(Integer.class), any(Integer.class), any(String.class));
    }

    @Test
    @DisplayName("Find Characters By Filters - Http Status 200 - USER OK")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void findCharactersByFilter2() throws Exception {
        List<CharacterDTOFilterResponse> response = List.of(generateFilterResponse());
        when(characterService.findCharactersByFilter(any(String.class), any(Integer.class), any(Integer.class), any(String.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/characters")
                        .param("name", "Billy")
                        .param("age", String.valueOf(28))
                        .param("weight", String.valueOf(80))
                        .param("movie", generateID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).findCharactersByFilter(any(String.class),
                any(Integer.class), any(Integer.class), any(String.class));
    }

    @Test
    @DisplayName("Find Characters By Filters - Http Status 403 - User without roles not allowed")
    void findCharactersByFilter3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disney/characters")
                        .param("name", "Billy")
                        .param("age", String.valueOf(28))
                        .param("weight", String.valueOf(80))
                        .param("movie", generateID()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(characterService);
    }

    @Test
    @DisplayName("Find Characters By Filters - Http Status 200 - ADMIN OK no path variables")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void findCharactersByFilter4() throws Exception {
        List<CharacterDTOFilterResponse> response = List.of();
        when(characterService.findCharactersByFilter(any(String.class), any(Integer.class), any(Integer.class), any(String.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/characters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print());

        verify(characterService, times(1)).findCharactersByFilter(eq(null), eq(null), eq(null), eq(null));
    }

    static CharacterDTOCreation generateDTORequest(){
        return CharacterDTOCreation.builder()
                .image("Character.png")
                .name("CharacterName")
                .age(30)
                .story("Character's story")
                .build();
    }

    static CharacterDTO generateCharacterDTO() {
        return CharacterDTO.builder()
                .image("Character.png")
                .name("CharacterName")
                .age(30)
                .story("Character's story")
                .movies(new ArrayList<>())
                .build();
    }

    static String generateID() {
        return UUID.randomUUID().toString();
    }

    static CharacterDTOFilterResponse generateFilterResponse() {
        return CharacterDTOFilterResponse.builder()
                .image("Character.png")
                .name("CharacterName")
                .build();
    }
}
