package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.Director.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.Film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.Genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.Mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private FilmRepository films;
    private UserRepository users;
    private MpaRepository mpaRepository;
    private GenreRepository genreRepository;
    private DirectorRepository directorRepository;

    @Override
    public Collection<Film> getAll() {
        log.info("Получение списка фильмов");
        return films.getAllFilms().values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        try {
            mpaRepository.isMpaExists(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ValidationException("Такого MPA не существует");
        }

        films.create(film);
        if (film.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        try {
            genreRepository.saveGenre(film);
            directorRepository.saveDirectorsToFilm(film);
        } catch (NotFoundException e) {
            throw new ValidationException("Такого жанра не существует");
        }

        log.info("Фильм {} добавлен в список с id = {}", film.getName(), film.getId());
        return film;
    }

    @Override
    public void update(Film film) {
        films.isFilmNotExists(film.getId());
        mpaRepository.isMpaExists(film.getMpa().getId());
        films.update(film);
        genreRepository.saveGenre(film);
        log.info("Фильм с id = {} обновлен", film.getId());
    }

    @Override
    public Film getById(long filmID) {
        log.info("Получение фильма с id = {}", filmID);
        return films.getById(filmID).orElseThrow(() -> new NotFoundException("Фильм с ID - " + filmID + " не найден"));
    }

    @Override
    public void addLike(Long filmID, Long userId) {
        films.isFilmNotExists(filmID);
        films.addLike(filmID, userId);
        log.info("Пользователь с id = {} поставил лайк фильму id = {}", userId, filmID);
    }

    @Override
    public void deleteLike(Long filmID, Long userId) {
        films.isFilmNotExists(filmID);
        users.isUserNotExists(userId);
        films.deleteLike(filmID, userId);
        log.info("Пользователь с id = {} удалил лайк фильму id = {}", userId, filmID);
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        log.info("Получение списка {} популярных фильмов", count);
        return films.getPopular(count).values().stream().toList();
    }

    @Override
    public Collection<Film> getSortedDirectorsFilms(Long directorId, String sortBy) {
        directorRepository.isDirectorNotExists(directorId);
        if (sortBy.equals("year")) {
            return films.getSortedDirectorsFilmsByYear(directorId);
        } else if (sortBy.equals("likes")) {
            return films.getSortedDirectorsFilmsByLikes(directorId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный запрос сортировки");
        }

    }

}
