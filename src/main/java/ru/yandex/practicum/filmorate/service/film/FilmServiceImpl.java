package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.Film.FilmRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private FilmRepository films;

    @Override
    public Collection<Film> getAll() {
        return films.getAllFilms();
    }

    @Override
    public Film create(Film film) {
        return films.create(film);
    }

    @Override
    public void update(Film film) {
        films.update(film);
    }

    @Override
    public Film getById(long filmID) {
        return films.getById(filmID).orElseThrow(() -> new NotFoundException("Фильм с ID - " + filmID + " не найден"));
    }

    @Override
    public void delete(long filmID) {
        films.delete(filmID);
    }

    @Override
    public void addLike(Long filmID, Long userId) {
        films.addLike(filmID, userId);
    }

    @Override
    public void deleteLike(Long filmID, Long userId) {
        films.deleteLike(filmID, userId);
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        return films.getPopular(count);
    }

}
