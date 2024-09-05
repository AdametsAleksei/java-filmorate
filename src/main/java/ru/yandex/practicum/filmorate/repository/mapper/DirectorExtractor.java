package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorExtractor implements ResultSetExtractor<Director> {


    @Override
    public Director extractData(ResultSet rs) throws SQLException, DataAccessException {
        Director director = null;
        while (rs.next()) {
            if (director == null) {
                director = new Director();
            }
            director.setId(rs.getLong("DIRECTOR_ID"));
            director.setName(rs.getString("DIRECTOR_NAME"));
        }
        return director;
    }
}
