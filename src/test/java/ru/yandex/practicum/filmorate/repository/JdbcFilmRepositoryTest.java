package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.Film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.Film.JdbcFilmGenreRepository;
import ru.yandex.practicum.filmorate.repository.Film.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.Mpa.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.repository.Mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.mapper.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRowMapper;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class, MpaRowMapper.class,
        JdbcMpaRepository.class, JdbcFilmGenreRepository.class, FilmGenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepositoryTest")
public class JdbcFilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    public static final Long TEST_FILM_ID = 101L;

    public Film getTestFilm() {
        return Film.builder()
                .id(TEST_FILM_ID)
                .name("TestName")
                .description("Test")
                .releaseDate(LocalDate.parse("1999-02-27"))
                .duration(100)
                .mpa(mpaRepository.getById(1).get())
                .genres(new LinkedHashSet<>())
                .build();
    }

    @Test
    @DisplayName("Фильм из БД должен быть таким же, как и фильм из метода")
    public void shouldReturnFilmWhenFindById() {
        Optional<Film> filmOptional = filmRepository.getById(TEST_FILM_ID);
        System.out.println(filmOptional.get());
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm());
    }

    @Test
    @DisplayName("Должен возвращаться список всех фильмов")
    public void shouldReturnListAllFilms() {
        assertEquals(3,filmRepository.getAllFilms().size());
    }

    @Test
    @DisplayName("Фильм должен добавляться в БД")
    public void shouldBeCreatedNewFilm() {
        Film newFilm = Film.builder()
                .name("NewFilmTest")
                .description("Test")
                .releaseDate(LocalDate.parse("1999-02-27"))
                .duration(100)
                .mpa(mpaRepository.getById(1).get())
                .genres(new LinkedHashSet<>())
                .build();
        filmRepository.create(newFilm);
        assertThat(filmRepository.getById(newFilm.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    @DisplayName("Фильм должен быть обновлен")
    public void shouldBeUpdatedFilm() {
        Film updateFilm = getTestFilm();
        updateFilm.setName("UpdatedFilmTest");
        updateFilm.setDescription("UpdatedDescription");
        filmRepository.update(updateFilm);
        assertThat(filmRepository.getById(TEST_FILM_ID))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updateFilm);
    }

    @Test
    @DisplayName("Фильм должен быть удален")
    public void shouldBeDeletedFilm() {
        int countFilm = filmRepository.getAllFilms().size();
        filmRepository.delete(102L);
        assertEquals(countFilm - 1,filmRepository.getAllFilms().size());
    }
}
