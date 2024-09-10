package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.getById(id);
    }

    @GetMapping("popular")
    public Collection<Film> getPopular(@Min(0) @RequestParam(defaultValue = "10", name = "count") Long count) {
        log.info("Запрошены популярные фильмы");
        return filmService.getPopular(count);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Запрошен список всех фильмов");
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма: start");
        film = filmService.create(film);
        log.info("Создан фильм - {}", film);
        return film;
    }

    @PutMapping("{filmID}/like/{userID}")
    public void like(@PathVariable Long filmID, @PathVariable Long userID) {
        log.info("Like start");
        filmService.addLike(filmID, userID);
        log.info("Добавлен лайк к фильму c ID - {}, пользователем с ID - {}", filmID, userID);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма: start");
        filmService.update(newFilm);
        log.info("Фильм с id - {}, обновлен", newFilm.getId());
        return newFilm;
    }

    @DeleteMapping("{filmID}/like/{userID}")
    public void deleteLike(@PathVariable Long filmID, @PathVariable Long userID) {
        log.info("DeleteLike start");
        filmService.deleteLike(filmID, userID);
        log.info("Убран лайк фильма c ID - {}, пользователем с ID - {}", filmID, userID);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedDirectorsFilms(@PathVariable("directorId") Long directorId,
                                              @RequestParam(value = "sortBy", required = false) String sortBy) {
        log.info("Запрошен вывод отсортированных фильмов");
        List<Film> sortedFilms = filmService.getSortedDirectorsFilms(directorId, sortBy);
        log.info("Вывод отсортированных фильмов отправлен");
        return sortedFilms;
    }

    @DeleteMapping("/{filmID}")
    public void deleteFilm(@PathVariable Long filmID) {
        log.info("Удаление фильма по ID - {}", filmID);
        filmService.deleteFilm(filmID);
    }
    @GetMapping("/search")
    public Collection<Film> search (@RequestParam @NotBlank String query, @RequestParam @NotBlank String by) {
        log.info("Пришел запрос на поиск по параметрам {}, {}",query, by);
        return filmService.search(query, by);
    }
}
