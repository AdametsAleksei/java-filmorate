package ru.yandex.practicum.filmorate.repository.Film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.FilmsExtractor;

import java.sql.Date;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmExtractor filmExtractor;
    private final FilmsExtractor filmsExtractor;

    @Override
    public Map<Long,Film> getAllFilms() {
        String sql = """
                     SELECT *
                     FROM FILMS AS f
                     LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                     LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                     LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
                     """;
        return jdbc.query(sql, Map.of(), filmsExtractor);
    }

    @Override
    public Optional<Film> getById(Long id) {
        String sql = """
                     SELECT *
                     FROM FILMS AS f
                     LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                     LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                     LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                     WHERE f.FILM_ID = :film_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource("film_id", id);
        return Optional.ofNullable(jdbc.query(sql, parameter, filmExtractor));
    }

    @Override
    public Film create(Film film) {
        String sql = """
                     INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
                     VALUES (:name, :description, :release_date, :duration, :mpa_id);
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameter, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        film.setId(id);
        return film;
    }

    @Override
    public void update(Film film) {
        String sql = """
                     UPDATE FILMS
                     SET NAME = :name,
                     DESCRIPTION = :description,
                     RELEASE_DATE = :release_date,
                     DURATION = :duration,
                     MPA_ID = :mpa_id
                     WHERE FILM_ID = :film_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());
        jdbc.update(sql, parameter);
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sql = """
                     INSERT INTO POPULAR (FILM_ID, USER_ID)
                     VALUES (:film_id, :user_id)
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String sql = """
                     DELETE FROM POPULAR
                     WHERE FILM_ID = :film_id
                           AND USER_ID = :user_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public Map<Long, Film> getPopular(Long count) {
        String sql = """
                     SELECT *
                     FROM (
                     SELECT p.FILM_ID,
                            COUNT(p.FILM_ID) AS countlike
                     FROM POPULAR AS p
                     GROUP BY FILM_ID
                     ORDER BY COUNT(FILM_ID) DESC
                     LIMIT :count
                     ) AS popular
                     JOIN FILMS AS f ON popular.FILM_ID = f.FILM_ID
                     JOIN RATING_MPA AS r ON f.MPA_ID = r.MPA_ID
                     JOIN FILM_GENRE AS fg ON fg.FILM_ID = f.FILM_ID
                     JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource("count", count);
        return jdbc.query(sql, parameter, filmsExtractor);
    }

    @Override
    public void isFilmNotExists(Long id) {
        if (getById(id).isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }



}