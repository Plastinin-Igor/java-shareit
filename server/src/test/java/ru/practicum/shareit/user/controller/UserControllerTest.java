package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.repository.UserRepository;

import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setup() {
        userDto = new UserDto(1L, "Igor Plastinin", "plastinin-i@ya.ru");
        userCreateDto = new UserCreateDto("Igor Plastinin", "plastinin-i@ya.ru");
        userUpdateDto = new UserUpdateDto(1L, "Igor Plastinin", "plastinin-i@ya.ru");
    }

    @Test
    void addUser() throws Exception {
        when(userService.addUser(userCreateDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).addUser(any());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(userUpdateDto, 1L)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).updateUser(userUpdateDto, 1L);
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);

    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())));

        verify(userService, times(1)).getUsers();
    }

}