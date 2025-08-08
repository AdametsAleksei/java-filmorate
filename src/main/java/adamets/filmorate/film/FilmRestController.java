package adamets.filmorate.film;

import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.model.dto.FilmCreateDto;
import adamets.filmorate.film.model.dto.FilmFullDto;
import adamets.filmorate.film.model.dto.FilmUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/films")
public class FilmRestController {

    private static final Logger log = LoggerFactory.getLogger(FilmRestController.class);

    private final FilmService filmService;

    @Autowired
    public FilmRestController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmFullDto> createFilm(@RequestBody @Valid FilmCreateDto film) {
        log.debug("==> Запрос на создание фильма {}", film.toString());
        FilmFullDto filmNew = filmService.createFilm(film);
        log.debug("<== Фильм создан {}", filmNew.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmNew);
    }

    @PutMapping
    public ResponseEntity<FilmFullDto> updateFilm(@RequestBody @Valid FilmUpdateDto film) {
        log.debug("==> Запрос на обновление фильма {}", film.toString());
        FilmFullDto filmUpdate = filmService.updateFilm(film);
        log.debug("<== Фильм обновлен {}", filmUpdate.toString());
        return ResponseEntity.status(HttpStatus.OK).body(filmUpdate);
    }

    @GetMapping
    public ResponseEntity<Iterable<Film>> findAllFilms() {
        log.debug("==> Запрос на получение всех фильмов");
        Iterable<Film> films = filmService.getAllFilms();
        log.debug("<== Фильмы получены");
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<FilmFullDto> getById(@PathVariable("id") @Min(1) Long id) {
        log.debug("==> Запрос на получение фильма с ID = {}", id);
        FilmFullDto film = filmService.getById(id);
        log.debug("<== Фильм с ID = {} получен", id);
        return ResponseEntity.status(200).body(film);
    }

    @PutMapping(path = "/{id:\\d+}/like/{userId:\\d+}")
    public ResponseEntity<Void> addLike(@PathVariable("id") @Min(1) Long filmId,
                                        @PathVariable("userId") @Min(1) Long userId) {
        log.debug("==> Add like to Film with ID - {}, from User with ID - {}", filmId, userId);
//        filmService.addLike(filmId, userId);
        log.debug("<== Added like to Film with ID - {}, from User with ID - {}", filmId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{id:\\d+}/like/{userId:\\d+}")
    public ResponseEntity<Void> removeLike(@PathVariable("id") @Min(1) Long filmId,
                                           @PathVariable("userId") @Min(1) Long userId) {
        log.debug("==> Remove like to Film with ID - {}, from User with ID - {}", filmId, userId);
//        filmService.removeLike(filmId, userId);
        log.debug("<== Removed like to Film with ID - {}, from User with ID - {}", filmId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @GetMapping(path = "/popular")
//    public ResponseEntity<Iterable<Film>> getPopularFilms(
//            @RequestParam(name = "count", defaultValue = "10") int count) {
//        log.debug("==> Get popular films, count - {}", count);
//        Iterable<Film> popularFilmsList = this.filmService.getPopularFilms(count);
//        log.debug("<== Retrieved popular films, count {}", count);
//        return ResponseEntity.status(HttpStatus.OK).body(popularFilmsList);
//    }
}