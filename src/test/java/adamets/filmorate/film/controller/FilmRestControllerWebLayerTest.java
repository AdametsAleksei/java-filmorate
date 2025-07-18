package adamets.filmorate.film.controller;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.service.FilmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmRestController.class)
public class FilmRestControllerWebLayerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    FilmService filmService;

    Film film1;

    Film film2;

    Film invalidFilm;

    Long validId;

    Long invalidId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        film1 = new Film(validId, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        film2 = new Film(2L, "Terminator 2 ", "Very brutal film",
                LocalDate.of(1988, 10, 26), 150);
        invalidFilm = new Film(null, "", "",
                LocalDate.of(1700, 1, 1), -1);
        invalidId = 100000L;
    }

    @Test
    void getAllFilms_ReturnsFilmsList() throws Exception {
        //given
        when(filmService.getAllFilms()).thenReturn(List.of(film1, film2));

        //when
        this.mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of(film1, film2))))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(validId))
                .andExpect(jsonPath("$[1].id").value(2L));

        //then
        verify(filmService).getAllFilms();
        verifyNoMoreInteractions(filmService);

    }

    @Test
    void createFilm_ValidFilm_ReturnsCreatedFilm() throws Exception {

        //given
        when(filmService.createFilm(film1)).thenReturn(film1);

        //when
        MvcResult result = this.mockMvc.perform(post("/films")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(film1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        assertThat(mapper.writeValueAsString(film1))
                .isEqualTo(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        verify(filmService).createFilm(film1);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void createFilm_InvalidFilm_ReturnsBadRequestWithListValidationExceptions() throws Exception {

        //when
        this.mockMvc.perform(post("/films")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(invalidFilm)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray());

        //then
        verifyNoInteractions(filmService);
    }

    @Test
    void updateFilm_ValidFilm_ReturnsUpdatedFilm() throws Exception {

        film1.setName("Updated Terminator");

        //given
        when(filmService.updateFilm(film1)).thenReturn(film1);

        //when
        MvcResult result = this.mockMvc.perform(put("/films")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(film1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        //then
        assertThat(mapper.writeValueAsString(film1))
                .isEqualTo(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        verify(filmService).updateFilm(film1);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void updateFilm_InvalidFilm_ReturnsBadRequestWithListValidationExceptions() throws Exception  {

        //when
        this.mockMvc.perform(put("/films")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andReturn();

        //then
        verifyNoInteractions(filmService);
    }

    @Test
    void getFilmById_ValidId_ReturnsFilm() throws Exception {

        //given
        when(this.filmService.getById(validId)).thenReturn(film1);

        var requestBuilder = MockMvcRequestBuilders.get("/films/1");

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(mapper.writeValueAsString(film1)).isEqualTo(result.getResponse().getContentAsString());

        verify(filmService).getById(validId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void getFilmById_InvalidId_ReturnsNotFoundException() throws Exception {

        when(filmService.getById(invalidId)).thenThrow(new NotFoundException(
                String.format("Film with ID - %d not found", invalidId)));

        //given
        var requestBuilder = MockMvcRequestBuilders.get(String.format("/films/%d", invalidId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(String.format("Film with ID - %d not found", invalidId));

        verify(filmService).getById(invalidId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void removeLike_ValidFilmIdAndUserId_ReturnsVoidBody() throws Exception{

        //given
        doNothing().when(filmService).removeLike(validId, validId);
        var requestBuilder = MockMvcRequestBuilders.delete(String.format("/films/%d/like/%d", validId, validId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEmpty();

        verify(filmService).removeLike(validId, validId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void removeLike_InvalidFilmId_ReturnsNotFoundException() throws Exception {

        //given
        doThrow(new NotFoundException(String.format("Film with ID - %d not found", invalidId)))
                .when(filmService).removeLike(invalidId, validId);
        var requestBuilder = MockMvcRequestBuilders.delete(String.format("/films/%d/like/%d", invalidId, validId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(String.format("Film with ID - %d not found", invalidId));

        verify(filmService).removeLike(invalidId, validId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void removeLike_InvalidUserId_ReturnsNotFoundException() throws Exception {

        //given
        doThrow(new NotFoundException(String.format("User with ID - %d not found", invalidId)))
                .when(filmService).removeLike(validId, invalidId);
        var requestBuilder = MockMvcRequestBuilders.delete(String.format("/films/%d/like/%d", validId, invalidId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains(String.format("User with ID - %d not found", invalidId));
        verify(filmService).removeLike(validId, invalidId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void addLike_ValidFilmIdAndUserId_ReturnsVoidBody() throws Exception{

        //given
        doNothing().when(filmService).addLike(validId, validId);
        var requestBuilder = MockMvcRequestBuilders.put(String.format("/films/%d/like/%d", validId, validId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isNoContent())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
        verify(filmService).addLike(validId, validId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void addLike_InvalidFilmId_ReturnsNotFoundException() throws Exception {

        //given
        doThrow(new NotFoundException(String.format("Film with ID - %d not found", invalidId)))
                .when(filmService).addLike(invalidId, validId);
        var requestBuilder = MockMvcRequestBuilders.put(String.format("/films/%d/like/%d", invalidId, validId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains(String.format("Film with ID - %d not found", invalidId));
        verify(filmService).addLike(invalidId, validId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void addLike_InvalidUserId_ReturnsNotFoundException() throws Exception {

        //given
        doThrow(new NotFoundException(String.format("User with ID - %d not found", invalidId)))
                .when(filmService).addLike(validId, invalidId);
        var requestBuilder = MockMvcRequestBuilders.put(String.format("/films/%d/like/%d", validId, invalidId));

        //when
        var result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains(String.format("User with ID - %d not found", invalidId));
        verify(filmService).addLike(validId, invalidId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void getPopularFilms_ReturnsListFilms() throws Exception {

        //given
        when(filmService.getPopularFilms(10)).thenReturn(List.of(film1, film2));
        var requestBuilder = MockMvcRequestBuilders.get("/films/popular");

        //when
        MvcResult result = this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andReturn();

        assertThat(mapper.writeValueAsString(List.of(film1, film2))).isEqualTo(
                result.getResponse().getContentAsString());

        verify(filmService).getPopularFilms(10);
        verifyNoMoreInteractions(filmService);
    }
}