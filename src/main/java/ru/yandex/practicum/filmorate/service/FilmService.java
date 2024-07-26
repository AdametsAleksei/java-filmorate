package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface FilmService {
    void addLike(Long filmID, Long userID);

    void deleteLike(Long filmID, Long userID);

    //    Не понимаю как более элегантно это сделать:(
    Set<Film> getPopular(int count);

    Collection<Film> getAll();

    Film create(Film film);

    Film update(Film newFilm);

    User checkUser(Long userID);

    Film get(Long filmID);

    Long getNextId();
}
