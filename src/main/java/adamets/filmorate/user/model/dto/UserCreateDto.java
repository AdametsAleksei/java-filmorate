package adamets.filmorate.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserCreateDto(@Email
                            String email,
                            @NotBlank
                            String login,
                            String name,
                            @Past
                            LocalDate birthday) {
}
