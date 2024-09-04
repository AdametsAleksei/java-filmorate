package ru.yandex.practicum.filmorate.repository.Mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    @Override
    public Collection<Mpa> getAll() {
        String sql = """
                     SELECT *
                     FROM RATING_MPA;
                     """;
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        String sql = """
                     SELECT *
                     FROM RATING_MPA
                     WHERE MPA_ID = :mpa_id;
                     """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("mpa_id", id);
        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, mapper));
    }

    @Override
    public void isMpaExists(int id) {
        try {
            getById(id).get().getName();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг MPA с id = " + id + " не найден!");
        }
    }
}
