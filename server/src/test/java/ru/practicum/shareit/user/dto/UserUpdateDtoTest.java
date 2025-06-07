package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class UserUpdateDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "Igor Plastinin", "plastinin@ya.ru");

        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);

        assertThat(userDtoSaved).hasJsonPath("$.id");
        assertThat(userDtoSaved).hasJsonPath("$.name");
        assertThat(userDtoSaved).hasJsonPath("$.email");

        assertThat(userDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(userDtoSaved).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    void testDeserialize() throws IOException {
        String json = "{\"id\":1,\"name\":\"Igor Plastinin\",\"email\":\"plastinin@ya.ru\"}";

        UserDto deserializedUserDto = jacksonTester.parseObject(json);

        assertThat(deserializedUserDto.getId()).isEqualTo(1L);
        assertThat(deserializedUserDto.getName()).isEqualTo("Igor Plastinin");
        assertThat(deserializedUserDto.getEmail()).isEqualTo("plastinin@ya.ru");
    }

}