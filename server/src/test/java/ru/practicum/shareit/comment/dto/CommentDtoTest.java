package ru.practicum.shareit.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        CommentDto commentDto = new CommentDto(1L, 1L, "Text comment", "User", LocalDateTime.now());

        JsonContent<CommentDto> dtoInputSaved = jacksonTester.write(commentDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.item");
        assertThat(dtoInputSaved).hasJsonPath("$.text");
        assertThat(dtoInputSaved).hasJsonPath("$.authorName");
        assertThat(dtoInputSaved).hasJsonPath("$.created");

    }

    @Test
    void testDeserialize() throws Exception {
        final long id = 1L;
        final long itemId = 1L;
        final String text = "Text comment";
        final String authorName = "User";
        final LocalDateTime created = LocalDateTime.now();

        String json = String.format("{" +
                        "  \"id\": %d,\n" +
                        "  \"item\": %d,\n" +
                        "  \"text\": \"%s\",\n" +
                        "  \"authorName\": \"%s\",\n" +
                        "  \"created\": \"%s\"\n" +
                        "}",
                id, itemId, text, authorName, created.toString());

        CommentDto deserializedCommentDto = jacksonTester.parseObject(json);

        assertThat(deserializedCommentDto.getId()).isEqualTo(id);
        assertThat(deserializedCommentDto.getItem()).isEqualTo(itemId);
        assertThat(deserializedCommentDto.getText()).isEqualTo(text);
        assertThat(deserializedCommentDto.getAuthorName()).isEqualTo(authorName);
        assertThat(deserializedCommentDto.getCreated()).isEqualTo(created);
    }
}