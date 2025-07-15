package adamets.filmorate.user.service;

import adamets.filmorate.user.model.User;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    Iterable<User> getAllUsers();
}
