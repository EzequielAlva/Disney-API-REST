package com.example.alkemy.disneyworld.controller;

import com.example.alkemy.disneyworld.auth.config.ApplicationConfig;
import com.example.alkemy.disneyworld.auth.config.SecurityConfiguration;
import com.example.alkemy.disneyworld.auth.jwt.JwtConfig;
import com.example.alkemy.disneyworld.auth.jwt.JwtService;
import com.example.alkemy.disneyworld.auth.user.UserServiceDetailsImpl;
import com.example.alkemy.disneyworld.dto.MovieDTOFilterResponse;
import com.example.alkemy.disneyworld.dto.MovieDTORequest;
import com.example.alkemy.disneyworld.dto.MovieDTOResponse;
import com.example.alkemy.disneyworld.service.MovieService;
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

import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(MovieController.class)
@Import({SecurityConfiguration.class, ApplicationConfig.class})
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserServiceDetailsImpl userServiceDetails;

    // ---------------------------------------------------------------------------
    // -------------------------- Create Movie --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName(value = "Create Movie - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@disney.com", roles = "ADMIN")
    void createMovie1() throws Exception {
        //given
        MovieDTORequest request = generateDTORequest();
        MovieDTOResponse response = generateDTOResponse();
        when(movieService.createMovie(any())).thenReturn(response);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print());

        //then
        verify(movieService).createMovie(any());
    }

    @Test
    @DisplayName(value = "Create Movie - Http Status 403 - role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void createMovie2() throws Exception {
        MovieDTORequest request = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName(value = "Create Movie - Http Status 403 - User without roles not allowed")
    void createMovie3() throws Exception {
        MovieDTORequest request = generateDTORequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }


    @Test
    @DisplayName(value = "Create Movie - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@disney.com", roles = "ADMIN")
    void createMovie4() throws Exception {
        //given
        MovieDTORequest request = generateDTORequest();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/disney/moviee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        //then
        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName(value = "Create Movie - Http Status 400 - Invalid Request (null)")
    @WithMockUser(username = "admin@disney.com", roles = "ADMIN")
    void createMovie5() throws Exception {
        //given
        MovieDTORequest request = null;

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        //then
        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Get Movie By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Get Movie By ID - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getMovieById1() throws Exception{
        String id = generateId();
        MovieDTOResponse response = generateDTOResponse();
        when(movieService.getMovieById(id)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(movieService).getMovieById(id);
    }

    @Test
    @DisplayName("Get Movie By ID - Http Status 200 - USER OK")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void getMovieById2() throws Exception{
        String id = generateId();
        MovieDTOResponse response = generateDTOResponse();
        when(movieService.getMovieById(id)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(movieService).getMovieById(id);
    }

    @Test
    @DisplayName("Get Movie By ID - Http Status 403 - User without roles not allowed")
    void getMovieById3() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movie/{id}", generateId()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Get Movie By ID - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void getMovieById4() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/disney/moviee/{id}", generateId()))
                .andExpect(status().isNotFound());

        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Delete Movie By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Delete Movie By ID - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteMovieById1() throws Exception{
        String id = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movie/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie has been deleted correctly"))
                .andDo(MockMvcResultHandlers.print());

        verify(movieService).deleteMovieById(id);
    }

    @Test
    @DisplayName(value = "Delete Movie By ID - Http Status 403 - role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void deleteMovieById2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movie/{id}", generateId()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName(value = "Delete Movie By ID - Http Status 403 - User without roles not allowed")
    void deleteMovieById3() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movie/{id}", generateId()))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName(value = "Delete Movie By ID - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteMovieById4() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/moviee/{id}", generateId()))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Delete Movie By ID --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Update Movie - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateMovie1() throws Exception{
        Map<Object, Object> mapRequest = generateMapRequest();
        MovieDTOResponse response = generateDTOResponse();
        when(movieService.updateMovie(any(String.class), any(HashMap.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/movie/{id}", generateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(movieService).updateMovie(any(String.class), any(HashMap.class));
    }

    @Test
    @DisplayName("Update Movie - Http Status 403 - role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void updateMovie2() throws Exception{
        Map<Object, Object> mapRequest = generateMapRequest();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/movie/{id}", generateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapRequest)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Update Movie - Http Status 403 - User without roles not allowed")
    void updateMovie3() throws Exception{
        Map<Object, Object> mapRequest = generateMapRequest();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/movie/{id}", generateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapRequest)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Update Movie - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateMovie4() throws Exception{
        Map<Object, Object> mapRequest = generateMapRequest();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/movvie/{id}", generateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapRequest)))
                .andExpect(status().isNotFound());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Update Movie - Http Status 400 - Invalid Request (null)")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateMovie5() throws Exception{
        Map<Object, Object> mapRequest = null;

        mockMvc.perform(MockMvcRequestBuilders.patch("/disney/movie/{id}", generateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapRequest)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Get All Movies --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Get All Movies - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getAllMovies1() throws Exception{
        when(movieService.getAllMovies()).thenReturn(List.of(generateDTOResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/all-movies"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    @DisplayName("Get All Movies - Http Status 200 - USER OK")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void getAllMovies2() throws Exception{
        when(movieService.getAllMovies()).thenReturn(List.of(generateDTOResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/all-movies"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    @DisplayName("Get All Movies - Http Status 403 - User without roles not allowed")
    void getAllMovies3() throws Exception{
        when(movieService.getAllMovies()).thenReturn(List.of(generateDTOResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/all-movies"))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Get All Movies - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getAllMovies4() throws Exception{
        when(movieService.getAllMovies()).thenReturn(List.of(generateDTOResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/all-mmovies"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Get Movies By Filters --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Get Movies By Filters - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getMoviesByFilters1() throws Exception {
        MovieDTOFilterResponse response = generateFilterResponse();
        when(movieService.getMoviesByFilters(any(String.class), any(String.class), any(String.class))).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movies")
                        .param("name", "MyMovie")
                        .param("genre", generateId())
                        .param("order", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))))
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).getMoviesByFilters(any(String.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Get Movies By Filters - Http Status 200 - USER OK")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void getMoviesByFilters2() throws Exception {
        MovieDTOFilterResponse response = generateFilterResponse();
        when(movieService.getMoviesByFilters(any(String.class), any(String.class), any(String.class))).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movies")
                        .param("name", "MyMovie")
                        .param("genre", generateId())
                        .param("order", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))))
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).getMoviesByFilters(any(String.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Get Movies By Filters - Http Status 403 - User without roles not allowed")
    void getMoviesByFilters3() throws Exception {
        MovieDTOFilterResponse response = generateFilterResponse();
        when(movieService.getMoviesByFilters(any(String.class), any(String.class), any(String.class))).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movies")
                        .param("name", "MyMovie")
                        .param("genre", generateId())
                        .param("order", "ASC"))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Get Movies By Filters - Http Status 404 - Incorrect URI")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getMoviesByFilters4() throws Exception {
        MovieDTOFilterResponse response = generateFilterResponse();
        when(movieService.getMoviesByFilters(any(String.class), any(String.class), any(String.class))).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/moviess")
                        .param("name", "MyMovie")
                        .param("genre", generateId())
                        .param("order", "ASC"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Get Movies By Filters - Http Status 200 - ADMIN OK without request params")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getMoviesByFilters5() throws Exception {
        MovieDTOFilterResponse response = generateFilterResponse();
        when(movieService.getMoviesByFilters(eq(null), eq(null), eq(null))).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/disney/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())))
                .andDo(MockMvcResultHandlers.print());

        //Default value in "order" = "ASC"
        verify(movieService, times(1)).getMoviesByFilters(eq(null), eq(null), eq("ASC"));
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Add Character To Movie --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Add Character To Movie - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void addCharacterToMovie1() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isOk())
                .andExpect(content().string("Character added correctly"))
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).addCharacterToMovie(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Add Character To Movie - Http Status 404 - Incorrect URI with bad idCharacter")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void addCharacterToMovie2() throws Exception {
        String idMovie = generateId();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, ""))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, null))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Add Character To Movie - Http Status 400 - Incorrect URI with bad idMovie")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void addCharacterToMovie3() throws Exception {
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", "", idCharacter))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", null, idCharacter))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Add Character To Movie - Http Status 403 - role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void addCharacterToMovie4() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Add Character To Movie - Http Status 403 - User without roles not allowed")
    void addCharacterToMovie5() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.post("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    // ---------------------------------------------------------------------------
    // -------------------------- Remove Character From Movie --------------------------
    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("Remove Character From Movie - Http Status 200 - ADMIN OK")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void removeCharacterFromMovie1() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isOk())
                .andExpect(content().string("Character removed correctly"))
                .andDo(MockMvcResultHandlers.print());

        verify(movieService, times(1)).removeCharacterFromMovie(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Remove Character From Movie - Http Status 404 - Incorrect URI with bad idCharacter")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void removeCharacterFromMovie2() throws Exception {
        String idMovie = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, ""))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, null))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Remove Character From Movie - Http Status 400 - Incorrect URI with bad idMovie")
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void removeCharacterFromMovie3() throws Exception {
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", "", idCharacter))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", null, idCharacter))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Remove Character From Movie - Http Status 403 - role USER not allowed")
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    void removeCharacterFromMovie4() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }

    @Test
    @DisplayName("Remove Character From Movie - Http Status 403 - User without roles not allowed")
    void removeCharacterFromMovie5() throws Exception {
        String idMovie = generateId();
        String idCharacter = generateId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/disney/movies/{idMovie}/characters/{idCharacter}", idMovie, idCharacter))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        verifyNoInteractions(movieService);
    }





    static String generateId(){
        return UUID.randomUUID().toString();
    }

    static MovieDTORequest generateDTORequest(){
        return MovieDTORequest.builder()
                .title("MyMovie")
                .image("MyMovie.jpg")
                .score(4)
                .creationDate(LocalDate.of(2009, 9, 12))
                .genres(new ArrayList<>())
                .characters(new ArrayList<>())
                .build();
    }

    static MovieDTOResponse generateDTOResponse(){
        MovieDTORequest request = generateDTORequest();
        return MovieDTOResponse.builder()
                .id(generateId())
                .title(request.getTitle())
                .image(request.getImage())
                .score(request.getScore())
                .creationDate(request.getCreationDate())
                .genres(new ArrayList<>())
                .characters(new ArrayList<>())
                .build();
    }

    static Map<Object, Object> generateMapRequest(){
        MovieDTORequest dtoRequest = generateDTORequest();
        Map<Object, Object> mapRequest = new HashMap<>();
        mapRequest.put("title", dtoRequest.getTitle());
        mapRequest.put("image", dtoRequest.getImage());
        mapRequest.put("score", dtoRequest.getScore());
        mapRequest.put("creationDate", dtoRequest.getCreationDate());
        return mapRequest;
    }

    static MovieDTOFilterResponse generateFilterResponse(){
        MovieDTORequest request = generateDTORequest();
        return MovieDTOFilterResponse.builder()
                .image(request.getImage())
                .title(request.getTitle())
                .creationDate(request.getCreationDate())
                .build();
    }
}
