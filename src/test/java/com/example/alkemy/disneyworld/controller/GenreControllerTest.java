package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.auth.config.ApplicationConfig;
import com.example.alkemy.disneyworld.auth.config.SecurityConfiguration;
import com.example.alkemy.disneyworld.auth.jwt.JwtAuthenticationFilter;
import com.example.alkemy.disneyworld.auth.jwt.JwtConfig;
import com.example.alkemy.disneyworld.auth.jwt.JwtService;
import com.example.alkemy.disneyworld.auth.user.UserServiceDetailsImpl;
import com.example.alkemy.disneyworld.dto.GenreDTO;
import com.example.alkemy.disneyworld.dto.GenreDTOResponse;
import com.example.alkemy.disneyworld.service.GenreService;
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

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import({SecurityConfiguration.class, ApplicationConfig.class, JwtAuthenticationFilter.class})
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private UserServiceDetailsImpl userServiceDetails;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Create Genre - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGenre1() throws Exception{
        GenreDTO request = generateDTORequest();
        GenreDTOResponse response = generateDTOResponse();
        when(genreService.createGenre(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print());

        verify(genreService, times(1)).createGenre(request);
    }

    @Test
    @DisplayName("Create Genre - Http Status 403 - Role USER not Allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void createGenre2() throws Exception{
        GenreDTO request = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(genreService);
    }

    @Test
    @DisplayName("Create Genre - Http Status 403 - User with no role not Allowed")
    void createGenre3() throws Exception{
        GenreDTO request = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(genreService);
    }

    @Test
    @DisplayName("Create Genre - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGenre4() throws Exception{
        GenreDTO request = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/genrre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(genreService);
    }

    @Test
    @DisplayName("Create Genre - Http Status 400 - Invalid Request")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createGenre5() throws Exception{
        GenreDTO request = null;

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(genreService);
    }

    static GenreDTO generateDTORequest(){
        return GenreDTO.builder()
                .name("MyGenre")
                .image("MyGenre.png")
                .build();
    }

    static GenreDTOResponse generateDTOResponse(){
        GenreDTO request = generateDTORequest();
        return GenreDTOResponse.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .image(request.getImage())
                .build();
    }
}
