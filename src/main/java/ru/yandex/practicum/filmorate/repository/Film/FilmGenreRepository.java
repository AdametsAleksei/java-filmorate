package ru.yandex.practicum.filmorate.repository.Film;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

public interface FilmGenreRepository {
    Collection<FilmGenre> findGenresOfFilms(String filmsId);
}