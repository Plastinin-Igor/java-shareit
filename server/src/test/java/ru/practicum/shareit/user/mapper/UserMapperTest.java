package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


class UserMapperTest {

    @Test
    void testToUser_WithFullData_ReturnsMappedObject() {

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Igor Plastinin");
        userDto.setEmail("plastinin@ya.ru");

        User mappedUser = UserMapper.toUser(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertEquals(mappedUser.getEmail(), userDto.getEmail());
    }

    @Test
    void testToUser_NullValues_ReturnsMappedObjectWithNullFields() {

        UserDto userDto = new UserDto();
        userDto.setId(null);
        userDto.setName("");
        userDto.setEmail(null);

        User mappedUser = UserMapper.toUser(userDto);

        assertNull(mappedUser.getId());
        assertEquals("", mappedUser.getName());
        assertNull(mappedUser.getEmail());
    }

    @Test
    void testToUser_PartialData_ReturnsMappedObjectWithPartialData() {

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Igor");
        userDto.setEmail(null);

        User mappedUser = UserMapper.toUser(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertNull(mappedUser.getEmail());
    }

}