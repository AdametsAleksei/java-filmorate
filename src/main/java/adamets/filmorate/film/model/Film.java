package adamets.filmorate.film.model;

import adamets.filmorate.exceptions.customValidAnnotation.MinDate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Film {

    private Integer id;

    @NotEmpty(message = "Не может быть пустым")
    private String name;

    @Size(max = 200, message = "Не должно превышать 200 знаков")
    private String description;

    @MinDate
    private LocalDate releaseDate;

    @Positive(message = "Не может быть отрицательной")
    private Integer duration;

}
