package adamets.filmorate.user.service;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.user.model.User;
import adamets.filmorate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return this.userRepository.createUser(user)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Something went wrong when user %s create", user.toString())));
    }

    @Override
    public User updateUser(User user) {
        return this.userRepository.updateUser(user)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с ID - %d не найден", user.getId())));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        return this.userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с ID - %d не найден", userId)));
    }

//    надо будет переделать, и подумать как лучше при работе с БД
    @Override
    public void addFriend(Long userId, Long friendId) {
        if (!this.userRepository.userExisted(userId)) {
            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", userId));
        }
        if (!this.userRepository.userExisted(friendId)) {
            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", friendId));
        }
        this.userRepository.addFriend(userId, friendId);
    }

    @Override
    public Iterable<User> getAllUsersFriends(Long userId) {
        if (!this.userRepository.userExisted(userId)) {
            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", userId));
        }
        return this.userRepository.getAllUsersFriends(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        this.userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с таким ID - %d не найден", userId)));
        this.userRepository.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с таким ID - %d не найден", friendId)));
        this.userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public Iterable<User> getAllCommonFriends(Long userId, Long otherId) {
        this.userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с таким ID - %d не найден", userId)));
        this.userRepository.getUserById(otherId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с таким ID - %d не найден", otherId)));
        return this.userRepository.getAllCommonFriends(userId, otherId);
    }
}
