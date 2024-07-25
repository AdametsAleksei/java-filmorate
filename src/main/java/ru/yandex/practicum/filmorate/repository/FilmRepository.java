package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void add(Film film);

    void delete(Film film);

    Optional<Film> get(Long id);

    Collection<Film> getAll();

}
