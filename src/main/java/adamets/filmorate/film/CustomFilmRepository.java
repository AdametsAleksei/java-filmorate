package adamets.filmorate.film;

import adamets.filmorate.film.model.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomFilmRepository {

    @Query("SELECT f FROM Film f " +
            "LEFT JOIN FETCH f.genres " +
            "LEFT JOIN FETCH f.mpa " +
            "WHERE f.id = :id")
    Optional<Film> findByIdWithMpaAndGenres(@Param("id") Long id);

    @Query("SELECT f FROM Film f " +
            "LEFT JOIN FETCH f.genres " +
            "LEFT JOIN FETCH f.mpa " +
            "ORDER BY f.id")
    Iterable<Film> findAllWithMpaAndGenresOrderById();
}
