package ru.yandex.practicum.filmorate.repository.Genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;

@Slf4j
@Repository
public class JdbcGenreRepository extends BaseDbRepository<Genre> implements GenreRepository {
    private static final String GENRES_FIND_ALL_QUERY = """
            SELECT *
            FROM GENRE;
            """;
    private static final String GENRES_FIND_BY_ID_QUERY = """
            SELECT *
            FROM GENRE
            WHERE GENRE_ID = ?;
            """;

    public JdbcGenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAll() {
        log.info("Получение списка жанров");
        return findMany(GENRES_FIND_ALL_QUERY);
    }

    @Override
    public Genre getById(int id) {
        log.info("Получение жанра с id = {}", id);
        return findOne(GENRES_FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден!"));
    }
}
