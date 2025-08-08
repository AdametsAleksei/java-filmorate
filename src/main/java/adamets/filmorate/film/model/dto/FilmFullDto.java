package adamets.filmorate.film.model.dto;

import adamets.filmorate.genre.Genre;
import adamets.filmorate.mpa.Mpa;

import java.time.LocalDate;
import java.util.Set;

public record FilmFullDto(Long id,
                          String name,
                          String description,
                          LocalDate releaseDate,
                          Integer duration,
                          Mpa mpa,
                          Set<Genre> genres) {
}
