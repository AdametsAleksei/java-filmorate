package adamets.filmorate.user.service;

import adamets.filmorate.user.model.User;
import adamets.filmorate.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return this.userRepository.updateUser(user);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }
}
