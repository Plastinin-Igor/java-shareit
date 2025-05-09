package ru.practicum.shareit.user.storage;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class UserInMemoryStorage implements UserStorage {

    private final Map<Long, User> users;

    public UserInMemoryStorage() {
        users = new HashMap<>();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        checkingForEmail(user.getEmail(), user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkingForEmail(user.getEmail(), user.getId());
        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        return user;
    }

    @Override
    public boolean deleteUser(long userId) {
        return (users.remove(userId) != null);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    private void checkingForEmail(String email, Long userId) {
        for (User user : users.values()) {
            if (!userId.equals(user.getId()) && user.getEmail().equals(email)) {
                log.error("Email {} is already in use.", email);
                throw new ValidationException("Email " + email + " is already in use.");
            }
        }
    }

}
