package adamets.filmorate.film.service;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    @Override
    public Iterable<Film> findAll() {
        return this.filmRepository.findAll();
    }

    @Override
    public Film createFilm(Film film) {
        return this.filmRepository.createFilm(film).orElseThrow(
                () -> new RuntimeException("Something went wrong")
        );
    }

    @Override
    public Film updateFilm(Film film) {
        return this.filmRepository.updateFilm(film).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с таким ID - %s не найден", film.getId()))
        );
    }

    @Override
    public Film getById(Integer id) {
        return this.filmRepository.getById(id).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с таким ID - %s не найден", id))
        );
    }

}
