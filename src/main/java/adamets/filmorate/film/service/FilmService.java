package adamets.filmorate.film.service;

import adamets.filmorate.film.model.Film;

public interface FilmService {

    Iterable<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getById(Integer id);
}
