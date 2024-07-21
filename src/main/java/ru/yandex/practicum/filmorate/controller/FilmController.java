package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрошен список всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма: start");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан фильм - {}", film);
        return films.get(film.getId());
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма: start");
        if (!films.containsKey(newFilm.getId()))  {
            log.warn("Фильм с id - {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с указанным id не найден");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм с id - {}, обновлен", newFilm.getId());
        log.info("Обновление фильма: success");
        return films.get(newFilm.getId());
    }

    private long getNextId() {
        return ++id;
    }
}
