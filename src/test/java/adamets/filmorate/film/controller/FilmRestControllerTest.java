package adamets.filmorate.film.controller;

import adamets.filmorate.controller.FilmRestController;
import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.model.Film;
import adamets.filmorate.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmRestControllerTest {

    @Mock
    FilmService filmService;

    @InjectMocks
    FilmRestController restFilmController;

    Film film1;
    Film film2;
    Long userId;

    @BeforeEach
    void setUp() {
        film1 = new Film(1L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        film2 = new Film(2L, "Terminator 2 ", "Very brutal film",
                LocalDate.of(1988, 10, 26), 150);
        userId = 1L;
    }

    @Test
    void givenValidFilm_whenCreateFilm_thenCreatedFilmRetrieved() {

        //given
        when(filmService.createFilm(film1))
                .thenReturn(film1);

        //when
        ResponseEntity<Film> filmCreated = restFilmController.createFilm(film1);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmCreated.getStatusCode());
        assertThat(film1).isEqualTo(filmCreated.getBody());
        verify(filmService).createFilm(film1);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void updateFilm_InvalidFilm_ReturnsValidationException() {

        //given
        when(filmService.updateFilm(film1))
                .thenReturn(film1);
        //when
        ResponseEntity<Film> filmUpdated = restFilmController.updateFilm(film1);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmUpdated.getStatusCode());
        assertThat(film1).isEqualTo(filmUpdated.getBody());
        verify(filmService).updateFilm(film1);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void findAllFilms_ReturnsFilmsList() {

        //given
        when(filmService.getAllFilms())
                .thenReturn(List.of(film1, film2));

        //when
        ResponseEntity<Iterable<Film>> films = restFilmController.findAllFilms();

        //then
        assertThat(HttpStatus.OK).isEqualTo(films.getStatusCode());
        assertThat(List.of(film1, film2)).containsExactlyElementsOf(films.getBody());
        verify(filmService).getAllFilms();
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void getById_ReturnsFilm() {

        //given
        when(filmService.getById(1L))
                .thenReturn(film1);

        //when
        ResponseEntity<Film> filmRetrieved = restFilmController.getById(1L);

        //then
        assertThat(HttpStatus.OK).isEqualTo(filmRetrieved.getStatusCode());
        assertThat(film1).isEqualTo(filmRetrieved.getBody());
        verify(filmService).getById(1L);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void addLike_ReturnsVoid() {
        //given
       doNothing().when(filmService).addLike(film1.getId(), userId);

       //when
        ResponseEntity<Void> answerRetrieved = restFilmController.addLike(film1.getId(), userId);

       //then
        assertThat(HttpStatus.NO_CONTENT).isEqualTo(answerRetrieved.getStatusCode());
        assertThat(answerRetrieved.getBody()).isNull();
        verify(filmService).addLike(film1.getId(), userId);
        verifyNoMoreInteractions(filmService);

    }

    @Test
    void addLike_InvalidFilmId_ThrowsNotFoundException() {

        //given
        Long invalidId = 100000L;
        doThrow(new NotFoundException(String.format("Invalid user ID - %d", invalidId)))
                .when(filmService).addLike(invalidId, userId);

        //then
        assertThatThrownBy(() -> restFilmController.addLike(invalidId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Invalid user ID - %d", invalidId));
        verify(filmService).addLike(-1L, userId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void addLike_InvalidUserId_ThrowsNotFoundException() {

        //given
        Long invalidUserId = -1L;
        doThrow(new NotFoundException(String.format("Invalid user ID - %d", invalidUserId)))
                .when(filmService).addLike(film1.getId(), invalidUserId);

        //then
        assertThatThrownBy(() -> filmService.addLike(film1.getId(), invalidUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Invalid user ID - %d", invalidUserId));
        verify(filmService).addLike(film1.getId(), invalidUserId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void removeLike_ReturnsVoid() {
        //given
       doNothing().when(filmService).removeLike(film1.getId(), userId);

       //when
        ResponseEntity<Void> answerRetrieved = restFilmController.removeLike(film1.getId(), userId);

       //then
        assertThat(HttpStatus.NO_CONTENT).isEqualTo(answerRetrieved.getStatusCode());
        assertThat(answerRetrieved.getBody()).isNull();
        verify(filmService).removeLike(film1.getId(), userId);
        verifyNoMoreInteractions(filmService);

    }

    @Test
    void removeLike_InvalidFilmId_ThrowsNotFoundException() {

        //given
        Long invalidId = -1L;
        doThrow(new NotFoundException(String.format("Invalid user ID - %d", invalidId)))
                .when(filmService).removeLike(invalidId, userId);

        //then
        assertThatThrownBy(() -> restFilmController.removeLike(invalidId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Invalid user ID - %d", invalidId));
        verify(filmService).removeLike(-1L, userId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void removeLike_InvalidUserId_ThrowsNotFoundException() {

        //given
        Long invalidUserId = -1L;
        doThrow(new NotFoundException(String.format("Invalid user ID - %d", invalidUserId)))
                .when(filmService).removeLike(film1.getId(), invalidUserId);

        //then
        assertThatThrownBy(() -> filmService.removeLike(film1.getId(), invalidUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Invalid user ID - %d", invalidUserId));
        verify(filmService).removeLike(film1.getId(), invalidUserId);
        verifyNoMoreInteractions(filmService);
    }

    @Test
    void getPopularFilms_WithSpecifiedCount_ReturnsExpectedFilms() {

        //given
        int count = 2;
        when(filmService.getPopularFilms(count))
                .thenReturn(List.of(film1, film2));

        //when
        ResponseEntity<Iterable<Film>> answerRetrieved = restFilmController.getPopularFilms(count);

        //then
        assertThat(HttpStatus.OK).isEqualTo(answerRetrieved.getStatusCode());
        assertThat(List.of(film1, film2)).containsExactlyElementsOf(answerRetrieved.getBody());
        verify(filmService).getPopularFilms(count);
        verifyNoMoreInteractions(filmService);
    }

}