package ru.practicum.shareit.comment.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class CommentMapperTest {

    @Test
    public void testStandardConversion() {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great product!");
        commentDto.setCreated(LocalDateTime.of(2023, 10, 1, 10, 0));

        Comment comment = CommentMapper.toComment(commentDto);

        assertThat(comment.getId()).isEqualTo(commentDto.getId());
        assertThat(comment.getText()).isEqualTo(commentDto.getText());
        assertThat(comment.getCreated()).isEqualTo(commentDto.getCreated());
    }

    @Test
    public void testPartialInformation() {

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Simple review");
        commentDto.setCreated(LocalDateTime.of(2023, 10, 1, 12, 0));

        Comment comment = CommentMapper.toComment(commentDto);

        assertThat(comment.getText()).isEqualTo(commentDto.getText());
        assertThat(comment.getCreated()).isEqualTo(commentDto.getCreated());
        assertThat(comment.getId()).isNull(); // ID не установлено
    }

    @Test
    public void testCriticalFieldAbsent() {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setCreated(LocalDateTime.of(2023, 10, 1, 14, 0));
        commentDto.setText(null); // Текст не задан

        Comment comment = CommentMapper.toComment(commentDto);

        assertThat(comment.getText()).isNull(); // Текст не передан
        assertThat(comment.getId()).isEqualTo(commentDto.getId());
        assertThat(comment.getCreated()).isEqualTo(commentDto.getCreated());
    }

}