package adamets.filmorate.film.controller;

import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.service.FilmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/films")
@RequiredArgsConstructor
@Slf4j
public class RestFilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        log.info("==> Запрос на создание фильма {}", film.toString());
        Film filmNew = filmService.createFilm(film);
        log.info("<== Фильм создан {}", filmNew.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmNew);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilmById(@RequestBody @Valid Film film) {
        log.info("==> Запрос на обновление фильма {}", film.toString());
        Film filmUpdate = filmService.updateFilm(film);
        log.info("<== Фильм обновлен {}", filmUpdate.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmUpdate);
    }

    @GetMapping
    public ResponseEntity<Iterable<Film>> findAllFilms() {
        log.info("==> Запрос на получение всех фильмов");
        Iterable<Film> films = filmService.findAll();
        log.info("<== Фильмы получены");
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable("id") @Min(1) Integer id) {
        log.info("==> Запрос на получение фильма с ID = {}", id);
        Film film = filmService.getById(id);
        log.info("<== Фильм с ID = {} получен", id);
        return ResponseEntity.status(200).body(film);
    }
}
