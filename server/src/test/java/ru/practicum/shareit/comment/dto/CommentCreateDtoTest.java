package ru.practicum.shareit.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentCreateDtoTest {

    @Autowired
    private JacksonTester<CommentCreateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        CommentCreateDto commentCreateDto = new CommentCreateDto("Text comment", new ItemDto(), new UserDto());

        JsonContent<CommentCreateDto> dtoInputSaved = jacksonTester.write(commentCreateDto);

        assertThat(dtoInputSaved).hasJsonPath("$.text");
        assertThat(dtoInputSaved).hasJsonPath("$.item");
        assertThat(dtoInputSaved).hasJsonPath("$.author");

    }
}