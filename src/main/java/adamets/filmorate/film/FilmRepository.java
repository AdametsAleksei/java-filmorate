package adamets.filmorate.film;

import adamets.filmorate.film.model.Film;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long>, CustomFilmRepository {

    @Override
    @EntityGraph(attributePaths = {"mpa", "genres"})
    @NonNull
    Optional<Film> findById(@NonNull Long id);
}
