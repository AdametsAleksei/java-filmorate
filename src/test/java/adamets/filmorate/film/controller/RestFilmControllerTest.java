package adamets.filmorate.film.controller;

import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.service.FilmService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestFilmControllerTest {

    @Mock
    FilmService filmService;

    @InjectMocks
    RestFilmController restFilmController;

    @Test
    void givenValidFilm_whenCreateFilm_thenCreatedFilmRetrieved() {

        //given
        Film film = new Film(1, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmService.createFilm(film))
                .thenReturn(film);

        //when
        ResponseEntity<Film> filmCreated = restFilmController.createFilm(film);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmCreated.getStatusCode());
        assertThat(film).isEqualTo(filmCreated.getBody());
        verify(filmService).createFilm(film);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void updateFilm_InvalidFilm_ReturnsValidationException() {

        //given
        Film film = new Film(1, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmService.updateFilm(film))
                .thenReturn(film);
        //when
        ResponseEntity<Film> filmUpdated = restFilmController.updateFilmById(film);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmUpdated.getStatusCode());
        assertThat(film).isEqualTo(filmUpdated.getBody());
        verify(filmService).updateFilm(film);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void findAllFilms_ReturnsFilmsList() {

        //given
        Film film1 = new Film(1, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        Film film2 = new Film(2, "Terminator 2 ", "Very brutal film",
                LocalDate.of(1988, 10, 26), 150);
        when(filmService.findAll())
                .thenReturn(List.of(film1, film2));

        //when
        ResponseEntity<Iterable<Film>> films = restFilmController.findAllFilms();

        //then
        assertThat(HttpStatus.OK).isEqualTo(films.getStatusCode());
        assertThat(List.of(film1, film2)).containsExactlyElementsOf(films.getBody());
        verify(filmService).findAll();
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void getById_ReturnsFilm() {

        //given
        Film film1 = new Film(1, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmService.getById(1))
                .thenReturn(film1);

        //when
        ResponseEntity<Film> filmRetrieved = restFilmController.getById(1);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmRetrieved.getStatusCode());
        assertThat(film1).isEqualTo(filmRetrieved.getBody());
        verify(filmService).getById(1);
        verifyNoMoreInteractions(filmService);
    }

}