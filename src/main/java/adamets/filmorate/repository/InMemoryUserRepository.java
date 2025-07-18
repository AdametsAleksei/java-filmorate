package adamets.filmorate.repository;

import adamets.filmorate.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users;
    private final Map<Long, Set<Long>> friendList;
    private Long id;

    public InMemoryUserRepository() {
        this.users = new HashMap<>();
        this.friendList = new HashMap<>();
        this.id = 0L;
    }

    @Override
    public Optional<User> createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        Long id = getNewId();
        user.setId(id);
        users.put(id, user);
        return Optional.of(users.get(id));
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return Optional.of(users.get(user.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        friendList.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friendList.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public boolean userExisted(Long id) {
        return this.users.containsKey(id);
    }

    @Override
    public Iterable<User> getAllUsersFriends(Long userId) {
        return Optional.ofNullable(friendList.get(userId))
                .orElse(Set.of())
                .stream()
                .map(users::get)
                .toList();
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        Optional.ofNullable(friendList.get(userId))
                        .ifPresent(set -> set.remove(friendId));
        Optional.ofNullable(friendList.get(friendId))
                        .ifPresent(set -> set.remove(userId));
    }

    @Override
    public Iterable<User> getAllCommonFriends(Long userId, Long otherId) {
        Set<Long> otherFriendList = Optional.ofNullable(friendList.get(otherId))
                .orElse(Set.of());
        return Optional.ofNullable(friendList.get(userId))
                .orElse(Set.of())
                .stream()
                .filter(otherFriendList::contains)
                .map(users::get)
                .toList();
    }

    private Long getNewId() {
        return ++this.id;
    }
}
