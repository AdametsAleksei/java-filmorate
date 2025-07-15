package adamets.filmorate.film.repository;

import adamets.filmorate.film.model.Film;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryFilmRepository implements FilmRepository {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

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
    public Optional<Film> getById(Integer id) {
        if (this.films.containsKey(id)) {
            return Optional.of(this.films.get(id));
        }
        return Optional.empty();
    }

    private Integer getNewId() {
        return ++this.id;
    }
}
