package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    UserDto addUser(UserCreateDto userCreateDto);

    UserDto updateUser(UserUpdateDto userUpdateDto, Long userId);

    void deleteUser(long userId);

    Collection<UserDto> getUsers();

    UserDto getUserById(long userId);

    User getUser(long userId);

    void emailUsageCheck(String email, Long userId);
}
