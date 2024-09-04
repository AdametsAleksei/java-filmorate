package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Component
public class FilmsExtractor  implements ResultSetExtractor<Map<Long, Film>> {
    @Override
    public Map<Long, Film> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> films = new LinkedHashMap<>();
        while (rs.next()) {
            if (!films.containsKey(rs.getLong("FILM_ID"))) {
                Film film = new Film();
                film.setId(rs.getLong("FILM_ID"));
                film.setName(rs.getString("NAME"));
                film.setDescription(rs.getString("DESCRIPTION"));
                film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
                film.setDuration(rs.getInt("DURATION"));
                film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
                film.setGenres(new LinkedHashSet<>());
                films.put(film.getId(), film);
            }
            Genre genre = new Genre();
            genre.setId(rs.getInt("GENRE_ID"));
            genre.setName(rs.getString("GENRE_NAME"));
            if (genre.getId() > 0) {
                films.get(rs.getLong("FILM_ID")).getGenres().add(genre);
            }
        }
        return films;
    }
}
