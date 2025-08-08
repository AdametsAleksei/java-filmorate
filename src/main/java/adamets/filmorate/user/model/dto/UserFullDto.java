package adamets.filmorate.user.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserFullDto (Long id,
                           String email,
                           String login,
                           String name,
                           LocalDate birthday){
}
