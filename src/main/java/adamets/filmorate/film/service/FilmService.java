package adamets.filmorate.film.service;

import adamets.filmorate.film.model.Film;

public interface FilmService {

    Iterable<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Iterable<Film> getPopularFilms(int count);
}
