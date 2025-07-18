package adamets.filmorate.repository;

import adamets.filmorate.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    Iterable<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void addFriend(Long id, Long friendId);

    boolean userExisted(Long id);

    Iterable<User> getAllUsersFriends(Long userId);

    void deleteFriend(Long userId, Long friendId);

    Iterable<User> getAllCommonFriends(Long userId, Long otherId);
}
