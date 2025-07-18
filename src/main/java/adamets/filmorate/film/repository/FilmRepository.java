package adamets.filmorate.film.repository;

import adamets.filmorate.film.model.Film;

import java.util.Optional;

public interface FilmRepository {

    Iterable<Film> findAll();

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> getById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Iterable<Film> getPopularFilms(int count);
}
