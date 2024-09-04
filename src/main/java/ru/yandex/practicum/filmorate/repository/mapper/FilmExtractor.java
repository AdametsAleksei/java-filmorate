package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmExtractor implements ResultSetExtractor<Film> {
    public Film extractData(final ResultSet rs) throws SQLException, DataAccessException {
        Film film = null;
        if (rs.next()) {
            film = new Film();
            film.setName(rs.getString("NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(rs.getInt("DURATION"));
            film.setId(rs.getLong("FILM_ID"));
            film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));

            int idGenre = rs.getInt("GENRE_ID");
            if (idGenre != 0) {
                do {
                    film.getGenres()
                            .add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
                } while (rs.next());
            }
        }
        return film;
    }
}