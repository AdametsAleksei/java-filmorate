package adamets.filmorate.user.repository;

import adamets.filmorate.user.model.User;

public interface UserRepository {

    User createUser(User user);

    User updateUser(User user);

    Iterable<User> getAllUsers();
}
