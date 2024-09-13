package ru.yandex.practicum.filmorate.repository.Director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.DirectorRowMapper;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcDirectorRepository implements DirectorRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final DirectorRowMapper mapper;

    @Override
    public void createDirector(Director director) {
        String sql = """
                INSERT INTO DIRECTORS (DIRECTOR_NAME)
                VALUES (:director_name);
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_name", director.getName());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameter, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        director.setId(id);
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        String sql = """
                SELECT *
                FROM DIRECTORS
                WHERE DIRECTOR_ID = :director_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("director_id", id);
        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, mapper));
    }

    @Override
    public Collection<Director> findAllDirectors() {
        String sql = """
                SELECT *
                FROM DIRECTORS;
                """;
        return jdbc.query(sql, mapper);
    }

    public void updateDirector(Director director) {
        String sql = """
                UPDATE DIRECTORS
                SET "DIRECTOR_NAME" = :director_name
                WHERE DIRECTOR_ID = :director_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_name", director.getName())
                .addValue("director_id", director.getId());
        jdbc.update(sql, parameter);
    }

    public void deleteDirector(Long id) {
        String sql = """
                DELETE FROM DIRECTORS
                WHERE DIRECTOR_ID = :director_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_id", id);
        jdbc.update(sql, parameter);
    }

    @Override
    public void isDirectorNotExists(Long id) {
        try {
            getDirectorById(id).get().getName();
        } catch (EmptyResultDataAccessException e) {
//            Иначе не проходят тесты, если реализовывать через ExceptionController
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Режиссер с id = " + id + " не найден");
        }
    }

    @Override
    public void saveDirectorsToFilm(Film film) {
        Long filmId = film.getId();
        String sqlDelete = """
                DELETE FROM FILM_DIRECTOR
                WHERE FILM_ID = :film_id;
                """;
        SqlParameterSource parameterDelete = new MapSqlParameterSource("film_id", filmId);
        jdbc.update(sqlDelete, parameterDelete);
        Set<Director> directors = film.getDirectors();
        SqlParameterSource[] batch = new MapSqlParameterSource[directors.size()];
        int count = 0;
        for (Director director : directors) {
            isDirectorNotExists(director.getId());
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("film_id", filmId)
                    .addValue("director_id", director.getId());
            batch[count++] = parameter;
        }
        String sqlInsert = """
                INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID)
                VALUES (:film_id, :director_id);
                """;
        jdbc.batchUpdate(sqlInsert, batch);
    }
}
