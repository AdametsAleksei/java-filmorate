package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage films;
    private Long id = 0L;

    public Collection<Film> getAll() {
        return films.getAll();
    }

    public Film create(Film film) {
        film.setId(getNextId());
        films.add(film);
        return film;
    }

    public Film update(Film newFilm) {
        Optional<Film> filmOptional = films.get(newFilm.getId());
        if (filmOptional.isPresent()) {
            films.add(newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("Фильм с указанным id не найден");
        }
    }

    private long getNextId() {
        return ++id;
    }
}
