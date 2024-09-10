package ru.yandex.practicum.filmorate.repository.Film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmRepository {
    Map<Long, Film> getAllFilms();

    Optional<Film> getById(Long id);

    Film create(Film film);

    void update(Film newFilm);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Map<Long, Film> getPopular(Long count);

    void deleteFilm(Long id);

    void isFilmNotExists(Long id);

    List<Film> recommendations(Long userId);

    Collection<Film> getSortedDirectorsFilmsByLikes(Long directorId);

    Collection<Film> getSortedDirectorsFilmsByYear(Long directorId);
}
