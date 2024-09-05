package ru.yandex.practicum.filmorate.repository.Director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.DirectorExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.DirectorRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcDirectorRepository implements DirectorRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final DirectorRowMapper mapper;
    private final DirectorExtractor extractor;

    @Override
    public Director createDirector(Director director) {
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
        return director;
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        String sql = """
                SELECT *
                FROM DIRECTORS
                WHERE DIRECTOR_ID = :director_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("director_id", id);
        return Optional.ofNullable(jdbc.query(sql, parameter, extractor));
    }

    @Override
    public List<Director> findAllDirectors() {
        String sql = """
                SELECT *
                FROM DIRECTORS;
                """;
        return jdbc.query(sql, mapper);
    }

    public Director updateDirector(Director director) {
        String sql = """
                UPDATE DIRECTORS
                SET "DIRECTOR_NAME" = :director_name
                WHERE DIRECTOR_ID = :director_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("director_name", director.getName())
                .addValue("director_id", director.getId());
        jdbc.update(sql, parameter);
        return director;
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
            if (getDirectorById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Режиссер с id = " + id + " не найден");
        }
    }

    @Override
    public void saveDirectorsToFilm(Film film) {
        Set<Director> directors = film.getDirectors();
        String sql = """
                INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID)
                VALUES (:film_id, :director_id);
                """;
        for (Director d : directors) {
            isDirectorNotExists(d.getId());
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("film_id", film.getId())
                    .addValue("director_id", d.getId());
            jdbc.update(sql, parameter);

        }
    }
}
