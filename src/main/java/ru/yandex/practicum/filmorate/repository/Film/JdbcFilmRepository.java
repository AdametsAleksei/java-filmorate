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
    public Map<Long, Film> getAllFilms() {
        String sql = """
                SELECT *
                FROM FILMS AS f
                LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                LEFT JOIN FILM_DIRECTOR FD on f.FILM_ID = FD.FILM_ID
                LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = FD.DIRECTOR_ID;
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
                LEFT JOIN FILM_DIRECTOR FD on f.FILM_ID = FD.FILM_ID
                LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = FD.DIRECTOR_ID
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
    public void addLike(Long filmId, Long userId) {
        String sql = """
                INSERT INTO POPULAR (FILM_ID, USER_ID)
                VALUES (:film_id, :user_id)
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", filmId)
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
                        SELECT f.FILM_ID,
                        f.NAME,
                        f.DESCRIPTION,
                        f.RELEASE_DATE,
                        f.DURATION,
                        r.MPA_ID,
                        r.MPA_NAME,
                        g.GENRE_ID,
                        g.GENRE_NAME,
                        fd.DIRECTOR_ID,
                        d.DIRECTOR_NAME
                FROM (
                        SELECT f.FILM_ID, COALESCE(countlike, 0) AS countlike
                        FROM (SELECT film_id, COALESCE(COUNT(film_id), 0) AS countlike
                                FROM POPULAR
                                GROUP BY film_id
                        ) AS ft
                        RIGHT JOIN FILMS AS f ON f.film_id = ft.FILM_ID
                        ORDER BY countlike DESC
                        LIMIT :count) AS ft
                JOIN FILMS AS f ON ft.film_id = f.FILM_ID
                JOIN RATING_MPA AS r ON f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON fg.FILM_ID = f.FILM_ID
                LEFT JOIN GENRE AS g ON fg.genre_id = g.GENRE_ID
                LEFT JOIN FILM_DIRECTOR AS fd ON f.FILM_ID = fd.FILM_ID
                LEFT JOIN DIRECTORS AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("count", count);
        return jdbc.query(sql, parameter, filmsExtractor);
    }

    @Override
    public void deleteFilm(Long id) {
        String sql = """
                DELETE FROM FILMS
                WHERE FILM_ID = :film_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("film_id", id);
        jdbc.update(sql, parameter);
    }

    @Override
    public void isFilmNotExists(Long id) {
        if (getById(id).isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    @Override
    public Collection<Film> getSortedDirectorsFilmsByLikes(Long directorId) {
        String sql = """
                SELECT
                f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,
                r.MPA_ID AS MPA_ID,
                r.MPA_NAME AS MPA_NAME,
                g.GENRE_ID, g.GENRE_NAME,
                d.DIRECTOR_ID, d.DIRECTOR_NAME,
                COUNT(p.FILM_ID) AS count
                FROM
                FILMS AS f
                LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                LEFT JOIN FILM_DIRECTOR AS fd ON f.FILM_ID = fd.FILM_ID
                LEFT JOIN DIRECTORS AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID
                LEFT JOIN POPULAR p on f.FILM_ID = p.FILM_ID
                WHERE
                f.FILM_ID IN (
                        SELECT FILM_ID
                FROM FILM_DIRECTOR
                WHERE DIRECTOR_ID = :director_id
                )
                GROUP BY f.FILM_ID , FD.DIRECTOR_ID
                ORDER BY count DESC;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_id", directorId);
        return Objects.requireNonNull(jdbc.query(sql, parameter, filmsExtractor)).values();
    }

    @Override
    public Collection<Film> getSortedDirectorsFilmsByYear(Long directorId) {
        String sql = """
                SELECT
                f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,
                r.MPA_ID AS MPA_ID,
                r.MPA_NAME AS MPA_NAME,
                g.GENRE_ID, g.GENRE_NAME,
                d.DIRECTOR_ID, d.DIRECTOR_NAME
                FROM
                FILMS AS f
                LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                LEFT JOIN FILM_DIRECTOR AS fd ON f.FILM_ID = fd.FILM_ID
                LEFT JOIN DIRECTORS AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID
                WHERE
                f.FILM_ID IN (
                        SELECT FILM_ID
                FROM FILM_DIRECTOR
                WHERE DIRECTOR_ID = :director_id
                )
                GROUP BY f.FILM_ID , FD.DIRECTOR_ID,f.RELEASE_DATE
                ORDER BY f.RELEASE_DATE;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_id", directorId);
        return Objects.requireNonNull(jdbc.query(sql, parameter, filmsExtractor)).values();
    }

    @Override
    public Collection<Film> getUserFilm(Long userId) {
        String sql = """
                     SELECT p.USER_ID,
                           f.FILM_ID,
                           f.NAME,
                           f.DESCRIPTION,
                           f.RELEASE_DATE,
                           f.DURATION,
                           f.MPA_ID,
                           r.MPA_NAME,
                           fg.GENRE_ID,
                           g.GENRE_NAME,
                           fd.DIRECTOR_ID,
                           d.DIRECTOR_NAME
                     FROM POPULAR AS p
                     LEFT JOIN FILMS AS f ON p.FILM_ID = f.FILM_ID
                     LEFT JOIN RATING_MPA AS r ON f.MPA_ID = r.MPA_ID
                     LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                     LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                     LEFT JOIN FILM_DIRECTOR AS fd ON f.FILM_ID = fd.FILM_ID
                     LEFT JOIN DIRECTORS AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID
                     WHERE p.USER_ID = :user_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource("user_id", userId);
        return Objects.requireNonNull(jdbc.query(sql, parameter, filmsExtractor)).values();
    }
}