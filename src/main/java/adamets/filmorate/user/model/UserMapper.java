package adamets.filmorate.user.model;

import adamets.filmorate.user.model.dto.UserCreateDto;
import adamets.filmorate.user.model.dto.UserFullDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromUserCreateDtoToUser(UserCreateDto user) {
        return User.builder()
                .email(user.email())
                .login(user.login())
                .name(user.name().isEmpty() ? user.login() : user.name())
                .birthday(user.birthday())
                .build();
    }

    public UserFullDto fromUserToUserFullDto(User user) {
        return UserFullDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}
