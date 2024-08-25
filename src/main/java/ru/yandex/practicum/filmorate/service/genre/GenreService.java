package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreService {
    Collection<Genre> getAll();

    Genre getById(int id);
}
