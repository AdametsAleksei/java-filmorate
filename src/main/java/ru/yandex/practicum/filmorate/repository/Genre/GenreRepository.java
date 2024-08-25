package ru.yandex.practicum.filmorate.repository.Genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreRepository {
    Collection<Genre> getAll();

    Genre getById(int id);
}
