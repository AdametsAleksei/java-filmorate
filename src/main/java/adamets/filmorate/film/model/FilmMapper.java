package adamets.filmorate.film.model;

import adamets.filmorate.film.model.dto.FilmCreateDto;
import adamets.filmorate.film.model.dto.FilmFullDto;
import adamets.filmorate.film.model.dto.FilmUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public Film fromCreateDtoToFilm(FilmCreateDto filmCreateDto) {
        return Film.builder()
                .name(filmCreateDto.name())
                .description(filmCreateDto.description())
                .releaseDate(filmCreateDto.releaseDate())
                .duration(filmCreateDto.duration())
                .mpa(filmCreateDto.mpa())
                .genres(filmCreateDto.genres())
                .build();
    }

    public FilmFullDto fromFilmToFullFilmDto(Film film) {
        return new FilmFullDto(film.getId(), film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getGenres());
    }

    public Film fromUpdateDtoToFilm(FilmUpdateDto film, Film oldFilm) {
        return Film.builder()
                .id(film.id())
                .name(film.name() == null ? oldFilm.getName() : film.name())
                .description(film.description() == null ? oldFilm.getDescription() : film.description())
                .releaseDate(film.releaseDate() == null ? oldFilm.getReleaseDate() : film.releaseDate())
                .duration(film.duration() == null ? oldFilm.getDuration() : film.duration())
                .mpa(film.mpa() == null ? oldFilm.getMpa() : film.mpa())
                .genres(film.genres() == null ? oldFilm.getGenres() : film.genres())
                .build();
    }
}
