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
public class FilmRestController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        log.info("==> Запрос на создание фильма {}", film.toString());
        Film filmNew = filmService.createFilm(film);
        log.info("<== Фильм создан {}", filmNew.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmNew);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        log.info("==> Запрос на обновление фильма {}", film.toString());
        Film filmUpdate = filmService.updateFilm(film);
        log.info("<== Фильм обновлен {}", filmUpdate.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmUpdate);
    }

    @GetMapping
    public ResponseEntity<Iterable<Film>> findAllFilms() {
        log.info("==> Запрос на получение всех фильмов");
        Iterable<Film> films = filmService.getAllFilms();
        log.info("<== Фильмы получены");
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Film> getById(@PathVariable("id") @Min(1) Long id) {
        log.info("==> Запрос на получение фильма с ID = {}", id);
        Film film = filmService.getById(id);
        log.info("<== Фильм с ID = {} получен", id);
        return ResponseEntity.status(200).body(film);
    }

    @PutMapping(path = "/{id:\\d+}/like/{userId:\\d+}")
    public ResponseEntity<Void> addLike(@PathVariable("id") @Min(1) Long filmId,
                                        @PathVariable("userId") @Min(1) Long userId) {
        log.info("==> Add like to Film with ID - {}, from User with ID - {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("<== Added like to Film with ID - {}, from User with ID - {}", filmId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{id:\\d+}/like/{userId:\\d+}")
    public ResponseEntity<Void> removeLike(@PathVariable("id") @Min(1) Long filmId,
                                           @PathVariable("userId") @Min(1) Long userId) {
        log.info("==> Remove like to Film with ID - {}, from User with ID - {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("<== Removed like to Film with ID - {}, from User with ID - {}", filmId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/popular")
    public ResponseEntity<Iterable<Film>> getPopularFilms(
            @RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("==> Get popular films, count - {}", count);
        Iterable<Film> popularFilmsList = this.filmService.getPopularFilms(count);
        log.info("<== Retrieved popular films, count {}", count);
        return ResponseEntity.status(HttpStatus.OK).body(popularFilmsList);
    }
}
