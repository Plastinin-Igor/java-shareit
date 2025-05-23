package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto addUser(UserCreateDto userCreateDto) {
        User user = UserMapper.toUserFromCreateDto(userCreateDto);
        return UserMapper.toUserDto(userStorage.addUser(user));
    }

    public UserDto updateUser(UserUpdateDto userUpdateDto, Long userId) {
        User oldUser = getUser(userId);
        User newUser = UserMapper.toUserFromUpdateDto(oldUser, userUpdateDto);
        return UserMapper.toUserDto(userStorage.updateUser(newUser));
    }

    public boolean deleteUser(long userId) {
        getUser(userId);
        return userStorage.deleteUser(userId);
    }

    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

    }

    public UserDto getUserById(long userId) {
        return UserMapper.toUserDto(getUser(userId));
    }

    private User getUser(long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.error("User with id: {} not found.", userId);
            throw new NotFoundException("User with id: " + userId + " not found.");
        }
        return user;
    }

}
