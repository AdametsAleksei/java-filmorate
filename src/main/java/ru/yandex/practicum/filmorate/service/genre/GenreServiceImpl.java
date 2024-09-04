package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.Genre.GenreRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Collection<Genre> getAll() {
        log.info("Получение списка жанров");
        return genreRepository.getAll();
    }

    @Override
    public Genre getById(int id) {
        log.info("Получение жанра с id = {}", id);
        genreRepository.isGenreNotExist(id);
        return genreRepository.getById(id).orElseThrow(() -> new NotFoundException("Жанр c ID - " + id + " не найден"));
    }
}
