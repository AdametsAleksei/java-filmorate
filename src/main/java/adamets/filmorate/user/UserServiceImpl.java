package adamets.filmorate.user;

import adamets.filmorate.exceptions.NotFoundException;
import adamets.filmorate.user.model.User;
import adamets.filmorate.user.model.UserMapper;
import adamets.filmorate.user.model.dto.UserCreateDto;
import adamets.filmorate.user.model.dto.UserFullDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserFullDto createUser(UserCreateDto user) {
        return userMapper.fromUserToUserFullDto(this.userRepository.save(userMapper.fromUserCreateDtoToUser(user)));
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с ID - %d не найден", userId)));
    }

//    надо будет переделать, и подумать как лучше при работе с БД
//    @Override
//    public void addFriend(Long userId, Long friendId) {
//        if (!this.userRepository.userExisted(userId)) {
//            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", userId));
//        }
//        if (!this.userRepository.userExisted(friendId)) {
//            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", friendId));
//        }
//        this.userRepository.addFriend(userId, friendId);
//    }
//
//    @Override
//    public Iterable<User> getAllUsersFriends(Long userId) {
//        if (!this.userRepository.userExisted(userId)) {
//            throw new NotFoundException(String.format("Пользователь с таким ID - %d не найден", userId));
//        }
//        return this.userRepository.getAllUsersFriends(userId);
//    }
//
//    @Override
//    public void deleteFriend(Long userId, Long friendId) {
//        this.userRepository.getUserById(userId)
//                .orElseThrow(() -> new NotFoundException(
//                        String.format("Пользователь с таким ID - %d не найден", userId)));
//        this.userRepository.getUserById(friendId)
//                .orElseThrow(() -> new NotFoundException(
//                        String.format("Пользователь с таким ID - %d не найден", friendId)));
//        this.userRepository.deleteFriend(userId, friendId);
//    }
//
//    @Override
//    public Iterable<User> getAllCommonFriends(Long userId, Long otherId) {
//        this.userRepository.getUserById(userId)
//                .orElseThrow(() -> new NotFoundException(
//                        String.format("Пользователь с таким ID - %d не найден", userId)));
//        this.userRepository.getUserById(otherId)
//                .orElseThrow(() -> new NotFoundException(
//                        String.format("Пользователь с таким ID - %d не найден", otherId)));
//        return this.userRepository.getAllCommonFriends(userId, otherId);
//    }
}
