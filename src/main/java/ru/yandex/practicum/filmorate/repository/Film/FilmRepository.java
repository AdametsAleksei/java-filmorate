package ru.yandex.practicum.filmorate.repository.Film;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.Map;
import java.util.Optional;

public interface FilmRepository {
    Map<Long,Film> getAllFilms();

    Optional<Film> getById(Long id);

    Film create(Film film);

    void update(Film newFilm);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Map<Long, Film> getPopular(Long count);

    void isFilmNotExists(Long id);
}
