package ru.practicum.shareit.comment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommentCreateDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    // Пройденная проверка: корректный комментарий
    @Test
    public void testValidCommentCreation() {
        CommentCreateDto validComment = createValidComment();

        Set<ConstraintViolation<CommentCreateDto>> violations = validator.validate(validComment);

        assertTrue(violations.isEmpty());
    }

    // Неверный текст комментария: пустое значение
    @Test
    public void testInvalidTextField() {
        CommentCreateDto invalidComment = createValidComment();
        invalidComment.setText("");

        Set<ConstraintViolation<CommentCreateDto>> violations = validator.validate(invalidComment);

        assertFalse(violations.isEmpty());
        assertEquals("The text of the comment must be specified", violations.iterator().next().getMessage());
    }

    // Отсутствие текста вообще
    @Test
    public void testMissingTextField() {
        CommentCreateDto invalidComment = createValidComment();
        invalidComment.setText(null);

        Set<ConstraintViolation<CommentCreateDto>> violations = validator.validate(invalidComment);

        assertFalse(violations.isEmpty());
        assertEquals("The text of the comment must be specified", violations.iterator().next().getMessage());
    }

    // Отсутствие вещи
    @Test
    public void testMissingItemDescription() {
        CommentCreateDto invalidComment = createValidComment();
        invalidComment.setItem(null);

        Set<ConstraintViolation<CommentCreateDto>> violations = validator.validate(invalidComment);

        assertFalse(violations.isEmpty());
        assertEquals("Item description must be specified", violations.iterator().next().getMessage());
    }

    // Отсутствие автора комментария
    @Test
    public void testMissingAuthor() {
        CommentCreateDto invalidComment = createValidComment();
        invalidComment.setAuthor(null);

        Set<ConstraintViolation<CommentCreateDto>> violations = validator.validate(invalidComment);

        assertFalse(violations.isEmpty());
        assertEquals("Author must be specified", violations.iterator().next().getMessage());
    }

    private CommentCreateDto createValidComment() {
        return new CommentCreateDto(
                "This is a great item!",
                new ItemDto(),
                new UserDto()
        );
    }
}