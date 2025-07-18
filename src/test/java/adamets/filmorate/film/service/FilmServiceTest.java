package adamets.filmorate.film.service;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.model.Film;
import adamets.filmorate.repository.FilmRepository;
import adamets.filmorate.service.FilmServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    FilmRepository filmRepository;

    @InjectMocks
    FilmServiceImpl filmService;

    @Test
    @DisplayName("Получение списка из нескольких элементов")
    void givenListOfTwoFilms_whenGetAllFilmsFilms_shouldReturnListOfTwoFilms() {

        //given
        Film film1 = new Film(1L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        Film film2 = new Film(2L, "asdsa", "asd", LocalDate.now(), 10);
        when(filmRepository.findAll())
                .thenReturn(List.of(film1, film2));

        //when
        Iterable<Film> list = filmService.getAllFilms();

        //then
        assertThat(list).containsExactly(film1, film2);
        verify(filmRepository).findAll();
        verifyNoMoreInteractions(filmRepository);
    }

    @Test
    @DisplayName("Получение пустого списка")
    void givenEmptySource_whenFindAll_Films_thenEmptyListRetrieved() {

        //given
        when(filmRepository.findAll())
                .thenReturn(List.of());

        //when
        Iterable<Film> list = filmService.getAllFilms();

        //then
        assertThat(list).isEmpty();

        verify(filmRepository).findAll();
        verifyNoMoreInteractions(filmRepository);

    }

    @Test
    @DisplayName("Создание фильма")
    void givenValidFilm_whenCreateFilm_thenCreatedFilmRetrieved() {

        //given
        Film film = new Film(1L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.createFilm(film))
                .thenReturn(Optional.of(film));

        //when
        Film newFilm = filmService.createFilm(film);

        //then
        assertThat(film).isEqualTo(newFilm);

        verify(filmRepository).createFilm(film);
        verifyNoMoreInteractions(filmRepository);
    }

    @Test
    void givenInvalidFilm_whenCreateFilm_thenRuntimeException() {

        //given
        Film film = new Film(1L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.createFilm(film))
                .thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> filmService.createFilm(film)).isInstanceOf(RuntimeException.class);
        verify(filmRepository).createFilm(film);
        verifyNoMoreInteractions(filmRepository);
    }

    @Test
    void givenValidFilm_whenUpdateFilm_thenUpdatedFilmRetrieved() {

        //given
        Film film = new Film(1L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.updateFilm(film))
                .thenReturn(Optional.of(film));

        //when
        Film updatedFilm = filmService.updateFilm(film);

        //then
        assertThat(film).isEqualTo(updatedFilm);

        verify(filmRepository).updateFilm(film);
        verifyNoMoreInteractions(filmRepository);

    }

    @Test
    void givenInvalidFilm_whenUpdatedFilm_thenThrownNotFoundException() {

        //given
        Film film = new Film(100L, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.updateFilm(film))
                .thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> filmService.updateFilm(film))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Фильм с таким ID - %s не найден", film.getId()));

        verify(filmRepository).updateFilm(film);
        verifyNoMoreInteractions(filmRepository);

    }

    @Test
    void givenValidFilmId_whenGetFilmById_thenFilmRetrieved() {

        //given
        Long id = 1L;
        Film film = new Film(id, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.getById(id)).thenReturn(Optional.of(film));

        //when
        Film recievedFilm = filmService.getById(id);

        //then
        assertThat(film).isEqualTo(recievedFilm);

        verify(filmRepository).getById(id);
        verifyNoMoreInteractions(filmRepository);

    }

    @Test
    void givenInvalidFilmId_whenGetFilmById_thenThrownNotFoundException() {

        //given
        Long id = 1L;
        Film film = new Film(id, "Terminator", "Very brutal film",
                LocalDate.of(1984, 10, 26), 150);
        when(filmRepository.getById(id))
                .thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> filmService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Фильм с таким ID - %s не найден", film.getId()));

        verify(filmRepository).getById(id);
        verifyNoMoreInteractions(filmRepository);

    }
}