package adamets.filmorate.genre;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "genre", schema = "films")
@Entity
@Getter
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "id - " + this.id + ", name - " + this.name;
    }
}
