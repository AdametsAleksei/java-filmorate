package ru.yandex.practicum.filmorate.repository.Film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;

@Repository
public class JdbcFilmGenreRepository extends BaseDbRepository<FilmGenre> implements FilmGenreRepository {
    private static final String GENRES_FIND_BY_FILM_ID_QUERY = """
            SELECT fg.FILM_ID, g.GENRE_ID, g.GENRE_NAME
                                                 FROM FILM_GENRE AS fg
                                                 JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                                                 WHERE FILM_ID IN (%s)
                                                 ORDER BY fg.FILM_ID, g.GENRE_ID;
            """;

    public JdbcFilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<FilmGenre> findGenresOfFilms(String filmsId) {
        return findMany(String.format(GENRES_FIND_BY_FILM_ID_QUERY, filmsId));
    }
}
