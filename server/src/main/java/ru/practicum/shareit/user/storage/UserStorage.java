package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    Collection<User> getUsers();

    User getUserById(long userId);
}
