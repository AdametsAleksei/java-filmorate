package adamets.filmorate.film.model;

import adamets.filmorate.genre.Genre;
import adamets.filmorate.mpa.Mpa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "films", schema = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "mpa",
            referencedColumnName = "id")
    private Mpa mpa;

    @ManyToMany
    @JoinTable(name = "film_genres",
            schema = "films",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;
}
