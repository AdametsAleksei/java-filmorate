package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class RecommendationsExtractor implements ResultSetExtractor<Map<Long, HashSet<Long>>> {
    @Override
    public Map<Long, HashSet<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, HashSet<Long>> data = new HashMap<>();
        while (rs.next()) {
            if (!data.containsKey(rs.getLong("USER_ID"))) {
                data.put(rs.getLong("USER_ID"), new HashSet<>());
            }
            data.get((rs.getLong("USER_ID"))).add(rs.getLong("FILM_ID"));
        }
        return data;
    }
}

