package ru.yandex.practicum.filmorate.repository.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;
import ru.yandex.practicum.filmorate.repository.Mpa.MpaRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Slf4j
@Repository
public class JdbcFilmRepository extends BaseDbRepository<Film> implements FilmRepository {

    private static final String FILMS_FIND_ALL_QUERY = """
            SELECT *
            FROM FILMS AS f
            LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID;
            """;
    private static final String FILMS_INSERT_QUERY = """
            INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
                        VALUES (?, ?, ?, ?, ?);
            """;
    private static final String FILMS_UPDATE_QUERY = """
            UPDATE FILMS
            SET NAME = ?,
                DESCRIPTION = ?,
                RELEASE_DATE = ?,
                DURATION = ?,
                MPA_ID = ?
            WHERE FILM_ID = ?;
            """;
    private static final String FILMS_FIND_BY_ID_QUERY = """
            SELECT *
            FROM FILMS AS f
            LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
            WHERE f.FILM_ID = ?;
            """;
    private static final String FILMS_ADD_LIKE_QUERY = """
            INSERT INTO POPULAR (FILM_ID, USER_ID)
                        VALUES (?, ?);
            """;
    private static final String FILMS_DELETE_LIKE_QUERY = """
            DELETE FROM POPULAR
            WHERE FILM_ID = ?
                AND USER_ID = ?;
            """;
    private static final String FILMS_GET_POPULAR_QUERY = """
            SELECT
                f.FILM_ID AS FILM_ID,
                f.NAME AS NAME,
                f.DESCRIPTION AS DESCRIPTION,
                f.RELEASE_DATE AS RELEASE_DATE,
                f.DURATION AS DURATION,
                r.MPA_ID AS MPA_ID,
                r.MPA_NAME AS MPA_NAME,
            COUNT(p.FILM_ID) AS count
            FROM FILMS AS f
            LEFT JOIN POPULAR AS p ON p.FILM_ID = f.FILM_ID
            LEFT JOIN RATING_MPA AS r ON f.MPA_ID = r.MPA_ID
            GROUP BY f.FILM_ID
            ORDER BY count DESC
            LIMIT ?;
            """;
    private static final String FILMS_DELETE_FILMS_GENRE_QUERY = """
            DELETE FROM FILM_GENRE
            WHERE FILM_ID = ?;
            """;
    private static final String FILMS_INSERT_FILMS_GENRE_QUERY = """
            INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
            VALUES (?, ?);
            """;
    private static final String FILMS_DELETE = """
            DELETE FROM FILMS
            WHERE FILM_ID = ?;
            """;
    private static final String GENRES_FIND_BY_IDS_QUERY = """
            SELECT GENRE_ID FROM GENRE WHERE GENRE_ID IN (%S)
            """;
    private final MpaRepository mpaRepository;
    private final FilmGenreRepository filmGenreRepository;

    public JdbcFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper,
                              MpaRepository mpaRepository, FilmGenreRepository filmGenreRepository) {
        super(jdbc, mapper);
        this.mpaRepository = mpaRepository;
        this.filmGenreRepository = filmGenreRepository;
    }


    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получение списка фильмов");
        Collection<Film> films = findMany(FILMS_FIND_ALL_QUERY);
        setFilmsGenres(films);
        return films;
    }

    @Override
    public Optional<Film> getById(Long id) {
        log.info("Получение фильма с id = {}", id);
        Collection<Film> films = findMany(FILMS_FIND_BY_ID_QUERY, id);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
        setFilmsGenres(films);
        return Optional.ofNullable(films.iterator().next());
    }

    @Override
    public Film create(Film film) {
        long id = insertGetKey(
                FILMS_INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        if (!mpaRepository.isMpaExists(film.getMpa().getId())) {
            throw new ValidationException("Рейтинг MPA с id = " + film.getMpa().getId() + " не найден!");
        }
        updateGenres(film.getGenres(), id);
        log.info("Фильм {} добавлен в список с id = {}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new NotFoundException("Id фильма должен быть указан");
        }
        if (isFilmExists(film.getId())) {
            update(
                    FILMS_UPDATE_QUERY,
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            delete(
                    FILMS_DELETE_FILMS_GENRE_QUERY,
                    film.getId()
            );
            updateGenres(film.getGenres(), film.getId());
            delete(
                    film.getId()
            );
            log.info("Фильм с id = {} обновлен", film.getId());
            return film;
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    @Override
    public void delete(Long id) {
        if (!isFilmExists(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        delete(FILMS_DELETE, id);
        log.info("Фильм с id = {} удален", id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (!isFilmExists(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        Film film = findOne(FILMS_FIND_BY_ID_QUERY, id).orElse(null);
        insert(FILMS_ADD_LIKE_QUERY, id, userId);
        assert film != null;
        log.info("Пользователь с id = {} поставил лайк фильму id = {}", userId, id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        if (!isFilmExists(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        Film film = findOne(FILMS_FIND_BY_ID_QUERY, id).orElse(null);
        delete(FILMS_DELETE_LIKE_QUERY, id, userId);
        assert film != null;
        log.info("Пользователь с id = {} удалил лайк фильму id = {}", userId, id);
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        log.info("Получение списка {} популярных фильмов", count);
        return findMany(FILMS_GET_POPULAR_QUERY, count);
    }

    @Override
    public boolean isFilmExists(Long id) {
        return findOne(FILMS_FIND_BY_ID_QUERY, id).isPresent();
    }

    public void checkGenresExists(Collection<Genre> genres) {
        if (genres.isEmpty()) {
            return;
        }
        List<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .toList();
        List<Integer> existingGenreIds = findExistingGenreIds(genreIds);
        for (Genre genre : genres) {
            if (!existingGenreIds.contains(genre.getId()))
                throw new ValidationException("Жанр с id = " + genre.getId() + " не найден!");
        }
    }

    private List<Integer> findExistingGenreIds(List<Integer> genreIds) {
        String inClause = String.join(",", Collections.nCopies(genreIds.size(), "?"));
        String query = String.format(GENRES_FIND_BY_IDS_QUERY, inClause);
        return jdbc.queryForList(query, Integer.class, genreIds.toArray());
    }


    private void setFilmsGenres(Collection<Film> films) {

        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        String filmsId = films.stream()
                .map(film -> film.getId().toString())
                .collect(Collectors.joining(", "));

        Collection<FilmGenre> filmGenres = filmGenreRepository.findGenresOfFilms(filmsId);

        for (FilmGenre filmGenre : filmGenres) {
            Film film = filmById.get(filmGenre.getFilmId());
            if (film != null) {
                film.getGenres().add(new Genre(filmGenre.getGenreId(), filmGenre.getGenre()));
            }
        }
    }

    private void updateGenres(Set<Genre> genres, Long id) {
        checkGenresExists(genres);
        if (!genres.isEmpty()) {
            jdbc.batchUpdate(
                    FILMS_INSERT_FILMS_GENRE_QUERY,
                    genres.stream()
                            .map(genre -> new Object[]{id, genre.getId()})
                            .collect(Collectors.toList()),
                    genres.size(),
                    (ps, argument) -> {
                        ps.setLong(1, (Long) argument[0]);
                        ps.setInt(2, (Integer) argument[1]);
                    }
            );
        }
    }
}