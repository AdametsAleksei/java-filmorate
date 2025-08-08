package adamets.filmorate.user;

import adamets.filmorate.user.model.User;
import adamets.filmorate.user.model.dto.UserCreateDto;
import adamets.filmorate.user.model.dto.UserFullDto;

public interface UserService {

    UserFullDto createUser(UserCreateDto user);

    User updateUser(User user);

    Iterable<User> getAllUsers();

    User getUserById(Long id);

//    void addFriend(Long id, Long friendId);
//
//    Iterable<User> getAllUsersFriends(Long id);
//
//    void deleteFriend(Long userId, Long friendId);
//
//    Iterable<User> getAllCommonFriends(Long userId, Long otherId);
}
