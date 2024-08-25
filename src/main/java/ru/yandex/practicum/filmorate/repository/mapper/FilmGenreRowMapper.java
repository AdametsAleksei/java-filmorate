package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {

    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .filmId(rs.getLong("FILM_ID"))
                .genreId(rs.getInt("GENRE_ID"))
                .genre(rs.getString("GENRE_NAME"))
                .build();
    }
}
