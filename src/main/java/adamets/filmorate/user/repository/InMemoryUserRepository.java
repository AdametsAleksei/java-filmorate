package adamets.filmorate.user.repository;

import adamets.filmorate.user.model.User;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Integer, User> users;
    private Integer id;

    public InMemoryUserRepository() {
        this.users = new HashMap<>();
        this.id = 0;
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        Integer id = getNewId();
        user.setId(id);
        users.put(id, user);
        return  users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ValidationException(String.format(
                    "Пользователя с таким id - %d не существует", user.getId()));
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        return users.values();
    }

    private Integer getNewId() {
        return ++this.id;
    }
}
