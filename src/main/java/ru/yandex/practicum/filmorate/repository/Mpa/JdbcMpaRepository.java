package ru.yandex.practicum.filmorate.repository.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcMpaRepository extends BaseDbRepository<Mpa> implements MpaRepository {
    private static final String MPA_FIND_ALL_QUERY = """
            SELECT *
            FROM RATING_MPA;
            """;
    private static final String MPA_FIND_BY_ID_QUERY = """
            SELECT *
            FROM RATING_MPA
            WHERE MPA_ID = ?;
            """;

    public JdbcMpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Mpa> getAll() {
        log.info("Получение списка рейтингов");
        return findMany(MPA_FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        log.info("Получение рейтинга с id = {}", id);
        return Optional.ofNullable(findOne(MPA_FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id = " + id + " не найден!")));
    }

    @Override
    public boolean isMpaExists(int id) {
        return findOne(MPA_FIND_BY_ID_QUERY, id).isPresent();
    }
}
