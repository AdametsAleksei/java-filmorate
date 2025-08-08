package adamets.filmorate.mpa;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "mpa", schema = "films")
@Entity
@Getter
public class Mpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "id - " + this.id + ", name - " + this.name;
    }
}
