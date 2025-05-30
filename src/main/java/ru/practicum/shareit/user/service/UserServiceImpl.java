package ru.practicum.shareit.user.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConstraintException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserCreateDto userCreateDto) {
        emailUsageCheck(userCreateDto.getEmail(), 0L);
        User user = UserMapper.toUserFromCreateDto(userCreateDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateDto userUpdateDto, Long userId) {
        User oldUser = getUser(userId);
        User newUser = UserMapper.toUserFromUpdateDto(oldUser, userUpdateDto);
        emailUsageCheck(newUser.getEmail(), newUser.getId());
        return UserMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toUserDto(getUser(userId));
    }

    @Override
    public User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found."));

    }

    @Override
    public void emailUsageCheck(String email, Long userId) {
        List<User> users = new ArrayList<>(userRepository.findAll());
        for (User user : users) {
            if (!userId.equals(user.getId()) && user.getEmail().equals(email)) {
                log.error("Email {} is already in use.", email);
                throw new ConstraintException("Email " + email + " is already in use.");
            }
        }
    }
}
