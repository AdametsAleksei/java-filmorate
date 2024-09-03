package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.Genre.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Collection<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Override
    public Genre getById(int id) {
        return genreRepository.getById(id);
    }
}
