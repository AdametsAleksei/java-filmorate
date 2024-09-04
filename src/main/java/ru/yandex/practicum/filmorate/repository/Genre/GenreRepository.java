package ru.yandex.practicum.filmorate.repository.Genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {
    Collection<Genre> getAll();

    Optional<Genre> getById(int id);

    void saveGenre(Film film);

    void isGenreNotExist(int id);
}
