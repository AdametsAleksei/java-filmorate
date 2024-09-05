package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Collection<Film> getAll();

    Film create(Film film);

    void update(Film newFilm);

    Film getById(long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopular(int year, int genreId, int count);

    List<Film> recommendations(Long userId);

    List<Film> getSortedDirectorsFilms(Long directorId, String sortBy);

    void deleteFilm(Long id);

    Collection<Film> search(String query, String by);

    Collection<Film> getCommonFilms(Long userId, Long friendId);

    Collection<Film> getCommonFilms(Long userId, Long friendId);
}
