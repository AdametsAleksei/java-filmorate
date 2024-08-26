package ru.yandex.practicum.filmorate.repository.Film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Collection<Film> getAllFilms();

    Optional<Film> getById(Long id);

    Film create(Film film);

    Film update(Film newFilm);

    void delete(Long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopular(Long count);
}
