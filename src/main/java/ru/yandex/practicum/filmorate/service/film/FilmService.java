package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<Film> getAll();

    Film create(Film film);

    void update(Film newFilm);

    Film getById(long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopular(Long count);

    Collection<Film> getCommonFilms(Long userId, Long friendId);
}
