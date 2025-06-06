package ru.practicum.shareit.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConstraintException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Тестовые данные
    private User user;
    private UserDto expectedUserDto;
    private UserDto expectedCreateUserDto;
    private UserCreateDto createDto;
    private UserUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Igor Plastinin", "plastinin@ya.ru");
        expectedUserDto = new UserDto(1L, "Updated Name", "updated@ya.ru");
        expectedCreateUserDto = new UserDto(1L, "Igor Plastinin", "plastinin@ya.ru");
        createDto = new UserCreateDto("Igor Plastinin", "plastinin@ya.ru");
        updateDto = new UserUpdateDto(1L, "Updated Name", "updated@ya.ru");
    }

    // Метод проверки добавления нового пользователя
    @Test
    void shouldAddNewUserSuccessfully() throws Exception {
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.addUser(createDto);
        assertEquals(expectedCreateUserDto, result);
    }

    // Метод проверки обновления существующего пользователя
    @Test
    void shouldUpdateExistingUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        UserDto updatedResult = userService.updateUser(updateDto, 1L);

        assertEquals(expectedUserDto, updatedResult);
    }

    // Метод проверки удаления пользователя
    @Test
    void shouldDeleteUserSuccessfully() throws Exception {

        Optional<User> existingUser = Optional.of(new User());
        when(userRepository.findById(1L)).thenReturn(existingUser);


        doNothing().when(userRepository).delete(any());
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(any());

    }

    // Метод проверки получения всех пользователей
    @Test
    void shouldGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        Collection<UserDto> users = userService.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    void shouldGetUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }


    // Метод проверки исключения при попытке добавить пользователя с существующей почтой
    @Test
    void shouldFailOnDuplicateEmailInAddUser() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        createDto.setEmail(user.getEmail());
        assertThrows(ConstraintException.class, () -> userService.addUser(createDto));
    }

    // Метод проверки исключения при попытке удалить несуществующего пользователя
    @Test
    void shouldFailToDeleteNonExistentUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }

    // Метод проверки, что почта занята другим пользователем
    @Test
    void testEmailUsageCheckEmailIsUsed() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail(user.getEmail()); // Занятый e-mail

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        String email = user.getEmail();

        // Ожидается исключение
        assertThrows(ConstraintException.class, () -> userService.emailUsageCheck(email, user.getId()),
                "E-mail used by other user must cause an exception");
    }

}