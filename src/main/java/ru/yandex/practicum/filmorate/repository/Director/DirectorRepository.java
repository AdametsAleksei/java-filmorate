package ru.yandex.practicum.filmorate.repository.Director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface DirectorRepository {
    void createDirector(Director director);

    Collection<Director> findAllDirectors();

    Optional<Director> getDirectorById(Long id);

    void updateDirector(Director director);

    void deleteDirector(Long id);

    void isDirectorNotExists(Long id);

    void saveDirectorsToFilm(Film film);
}
