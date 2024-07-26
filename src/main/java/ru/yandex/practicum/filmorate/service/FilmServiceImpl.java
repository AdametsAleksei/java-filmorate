package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository films;
    private final UserRepository users;
    private Long id = 0L;
    private final Map<Film, Set<Long>> likes = new HashMap<>();

    @Override
    public void addLike(Long filmID, Long userID) {
        get(filmID);
        checkUser(userID);
        Set<Long> whoLike = likes.computeIfAbsent(get(filmID), id -> new HashSet<>());
        whoLike.add(userID);
    }

    @Override
    public void deleteLike(Long filmID, Long userID) {
        get(filmID);
        checkUser(userID);
        Set<Long> whoLike = likes.computeIfAbsent(get(filmID), id -> new HashSet<>());
        if (!whoLike.isEmpty()) {
            whoLike.remove(userID);
        } else {
            throw new NotFoundException("404 user don't likes this film");
        }
    }

//    Не понимаю как более элегантно это сделать:(
    @Override
    public Set<Film> getPopular(int count) {
        Map<Film, Integer> mapSort = new HashMap<>();
        for (Map.Entry<Film, Set<Long>> filmSetEntry : likes.entrySet()) {
            mapSort.put(filmSetEntry.getKey(), filmSetEntry.getValue().size());
        }
        Map<Film, Integer> mapReturn = new LinkedHashMap<>();
        mapSort.entrySet()
                .stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .limit(count)
                .forEach(entry -> mapReturn.put(entry.getKey(), entry.getValue()));
        return mapReturn.keySet();
    }

    @Override
    public Collection<Film> getAll() {
        return films.getAll();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.add(film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        get(newFilm.getId());
        films.add(newFilm);
        return newFilm;
    }

    @Override
    public User checkUser(Long userID) {
        return users.get(userID).orElseThrow(() -> new NotFoundException(
                "Пользователь c ID - " + userID + " не найден"));
    }

    @Override
    public Film get(Long filmID) {
       return films.get(filmID).orElseThrow(() -> new NotFoundException("Фильм с ID - " + filmID + " не найден"));
    }

    @Override
    public Long getNextId() {
        return ++id;
    }
}
