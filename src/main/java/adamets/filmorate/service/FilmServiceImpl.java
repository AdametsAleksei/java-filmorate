package adamets.filmorate.service;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.model.Film;
import adamets.filmorate.repository.FilmRepository;
import adamets.filmorate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public Iterable<Film> getAllFilms() {
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
        return this.filmRepository.updateFilm(film)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с таким ID - %s не найден", film.getId())));
    }

    @Override
    public Film getById(Long filmId) {
        return this.filmRepository.getById(filmId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с таким ID - %s не найден", filmId)));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        this.filmRepository.getById(filmId).orElseThrow(() -> new NotFoundException(
                String.format("Фильм с таким ID - %s не найден", filmId)));
        this.userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with ID - %s not found", userId)));
        this.filmRepository.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        this.filmRepository.getById(filmId).orElseThrow(() -> new NotFoundException(
                String.format("Фильм с таким ID - %s не найден", filmId)));
        this.userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with ID - %s not found", userId)));
        this.filmRepository.removeLike(filmId, userId);
    }

    @Override
    public Iterable<Film> getPopularFilms(int count) {
        return this.filmRepository.getPopularFilms(count);
    }

}
