package ru.yandex.practicum.filmorate.repository.Genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final GenreRowMapper mapper;

    @Override
    public Collection<Genre> getAll() {
        String sql = """
                     SELECT *
                     FROM GENRE;
                     """;
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<Genre> getById(int id) {
        String sql = """
                     SELECT *
                     FROM GENRE
                     WHERE GENRE_ID = :genre_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("genre_id", id);
        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, mapper));
    }

    @Override
    public void saveGenre(Film film) {
        Long filmId = film.getId();
        String sqlDelete = """
                           DELETE FROM FILM_GENRE
                           WHERE FILM_ID = :film_id;
                           """;
        SqlParameterSource parameterDelete = new MapSqlParameterSource("film_id", filmId);
        jdbc.update(sqlDelete, parameterDelete);
        Set<Genre> genres = film.getGenres();
        SqlParameterSource[] batch = new MapSqlParameterSource[genres.size()];
        int count = 0;
        for (Genre genre : genres) {
            isGenreNotExist(genre.getId());
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("film_id", filmId)
                    .addValue("genre_id", genre.getId());
            batch[count++] = parameter;
        }
        String sqlInsert = """
                           INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
                           VALUES (:film_id, :genre_id);
                           """;
        jdbc.batchUpdate(sqlInsert, batch);
    }

    @Override
    public void isGenreNotExist(int id) {
        try {
            getById(id).get().getName();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
    }
}
