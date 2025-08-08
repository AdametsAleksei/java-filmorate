package adamets.filmorate.film;

import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.model.dto.FilmCreateDto;
import adamets.filmorate.film.model.dto.FilmFullDto;
import adamets.filmorate.film.model.dto.FilmUpdateDto;

public interface FilmService {

    Iterable<Film> getAllFilms();

    FilmFullDto createFilm(FilmCreateDto film);

    FilmFullDto updateFilm(FilmUpdateDto film);

    FilmFullDto getById(Long id);

//    void addLike(Long filmId, Long userId);
//
//    void removeLike(Long filmId, Long userId);
//
//    Iterable<Film> getPopularFilms(int count);
}
