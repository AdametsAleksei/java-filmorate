package ru.yandex.practicum.filmorate.repository.Director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {

    Director createDirector(Director director);

    List<Director> findAllDirectors();

    Optional<Director> getDirectorById(Long id);

    Director updateDirector(Director director);

    void deleteDirector(Long id);

    void isDirectorNotExists(Long id);

    void saveDirectorsToFilm(Film film);
}
