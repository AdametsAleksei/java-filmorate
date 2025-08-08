package adamets.filmorate.film;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.film.model.Film;
import adamets.filmorate.film.model.FilmMapper;
import adamets.filmorate.film.model.dto.FilmCreateDto;
import adamets.filmorate.film.model.dto.FilmFullDto;
import adamets.filmorate.film.model.dto.FilmUpdateDto;
import adamets.filmorate.genre.Genre;
import adamets.filmorate.genre.GenreService;
import adamets.filmorate.mpa.MpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final FilmMapper filmMapper;

    @Override
    public Iterable<Film> getAllFilms() {
        return this.filmRepository.findAllWithMpaAndGenresOrderById();
    }

    @Override
    @Transactional
    public FilmFullDto createFilm(FilmCreateDto film) {
        Film entity = filmMapper.fromCreateDtoToFilm(film);
        entity.setMpa(mpaService.findById(film.mpa().getId()));
        Set<Genre> newGenres = Optional.ofNullable(film.genres()).orElse(Collections.emptySet());
        validateGenres(newGenres,
                genreService.findAllById(newGenres.stream()
                        .map(Genre::getId)
                        .toList())
        );
        entity.setGenres(newGenres);
        return filmMapper.fromFilmToFullFilmDto(filmRepository.save(entity));
    }

    @Override
    @Transactional
    public FilmFullDto updateFilm(FilmUpdateDto film) {
        Set<Genre> newGenres = Optional.ofNullable(film.genres()).orElse(Collections.emptySet());
        validateGenres(newGenres,
                genreService.findAllById(newGenres.stream()
                .map(Genre::getId)
                .toList())
        );
        Film oldFilm = this.filmRepository.findById(film.id())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с таким ID - %d не найден", film.id())));
        return filmMapper.fromFilmToFullFilmDto(this.filmRepository.save(
                filmMapper.fromUpdateDtoToFilm(film, oldFilm)));
    }

    @Override
    @Transactional(readOnly = true)
    public FilmFullDto getById(Long filmId) {
        return filmMapper.fromFilmToFullFilmDto(this.filmRepository.findByIdWithMpaAndGenres(filmId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с таким ID - %s не найден", filmId))));
    }

//    @Override
//    public void addLike(Long filmId, Long userId) {
//        this.filmRepository.findById(filmId).orElseThrow(() -> new NotFoundException(
//                String.format("Фильм с таким ID - %s не найден", filmId)));
//        this.userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
//                String.format("User with ID - %s not found", userId)));
//        this.filmRepository.save(filmId, userId);
//    }

//    @Override
//    public void removeLike(Long filmId, Long userId) {
//        this.filmRepository.findById(filmId).orElseThrow(() -> new NotFoundException(
//                String.format("Фильм с таким ID - %s не найден", filmId)));
//        this.userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
//                String.format("User with ID - %s not found", userId)));
//        this.filmRepository.removeLike(filmId, userId);
//    }

//    @Override
//    public Iterable<Film> getPopularFilms(int count) {
//        return this.filmRepository.getPopularFilms(count);
//    }

    private static void validateGenres(Set<Genre> filmGenres, Set<Genre> genresFromRepository) {
        Set<Integer> genresIdToCheck = filmGenres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        Set<Integer> genresIdFromRepository = genresFromRepository.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        genresIdToCheck.removeAll(genresIdFromRepository);
        if (!genresIdToCheck.isEmpty()) {
            throw new NotFoundException("Genres not found : " + genresIdToCheck);
        }
    }

}
