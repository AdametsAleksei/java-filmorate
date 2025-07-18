package adamets.filmorate.user.service;

import adamets.filmorate.user.model.User;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    Iterable<User> getAllUsers();

    User getUserById(Long id);

    void addFriend(Long id, Long friendId);

    Iterable<User> getAllUsersFriends(Long id);

    void deleteFriend(Long userId, Long friendId);

    Iterable<User> getAllCommonFriends(Long userId, Long otherId);
}
