package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Received a request to add a user.");
        UserDto userDto = userService.addUser(userCreateDto);
        log.info("User with id: {} added.", userDto.getId());
        return userDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto,
                              @PathVariable Long userId) {
        log.info("Received a request to modify a user with id: {}.", userId);
        userUpdateDto.setId(userId);
        UserDto userDto = userService.updateUser(userUpdateDto, userId);
        log.info("User with id: {} modified.", userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Received a request to delete user with id: {}.", userId);
        userService.deleteUser(userId);
        log.info("User with id: {} deleted.", userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Received a request to get a user with id: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

}
