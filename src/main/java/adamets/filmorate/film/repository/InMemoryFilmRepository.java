package adamets.filmorate.film.repository;

import adamets.filmorate.film.model.Film;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryFilmRepository implements FilmRepository {

    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<Long>> likes = new HashMap<>();
    private Long id = 0L;

    @Override
    public Iterable<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        film.setId(getNewId());
        films.put(film.getId(), film);
        return Optional.of(films.get(film.getId()));
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(films.get(film.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Film> getById(Long id) {
        if (this.films.containsKey(id)) {
            return Optional.of(this.films.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        this.likes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Optional.ofNullable(this.likes.get(filmId))
                .ifPresent(set -> set.remove(userId));
    }

    @Override
    public Iterable<Film> getPopularFilms(int count) {
        return this.likes.entrySet()
                .stream()
                .sorted(Comparator.comparingInt((Map.Entry<Long, Set<Long>> s) -> s.getValue().size()).reversed())
                .map(s -> films.get(s.getKey()))
                .toList();
    }

    private Long getNewId() {
        return ++this.id;
    }
}
