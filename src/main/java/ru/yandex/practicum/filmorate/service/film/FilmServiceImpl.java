package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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
            directorRepository.saveDirectorsToFilm(film);
        } catch (NotFoundException e) {
            throw new ValidationException("Такого режиссера не существует");
        }
        try {
            genreRepository.saveGenre(film);
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
        users.isUserNotExists(userId);
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
    public List<Film> getSortedDirectorsFilms(Long directorId, String sortBy) {
        directorRepository.isDirectorNotExists(directorId);
        if (sortBy.equals("year")) {
            return films.getSortedDirectorsFilmsByYear(directorId).stream().toList();
        } else if (sortBy.equals("likes")) {
            return films.getSortedDirectorsFilmsByLikes(directorId).stream().toList();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный запрос сортировки");
        }
    }
    @Override
    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        users.isUserNotExists(userId);
        users.isUserNotExists(friendId);
        try {
            List<Film> userFilms = new ArrayList<>(films.getUserFilm(userId).stream().toList());
            List<Film> friendFilms = new ArrayList<>(films.getUserFilm(friendId).stream().toList());
            friendFilms.retainAll(userFilms);
            log.info("Получены общие фильмы для пользователя с id = {} и пользователя с id = {}", userId, friendId);
            return friendFilms;
        } catch (NullPointerException e) {
            throw new NotFoundException("У пользователей нет общих фильмов");
        }
    }
}
