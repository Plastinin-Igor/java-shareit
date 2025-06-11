package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateDtoTest {


    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Корректные данные пользователя
    @Test
    public void testValidUser() {
        UserCreateDto user = new UserCreateDto("Igor Plastinin", "plastinin@ya.ru");

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    // Пустое имя пользователя - ошибка
    @Test
    public void testInvalidWithEmptyName() {

        UserCreateDto user = new UserCreateDto("", "plastinin@ya.ru");

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals("Username must be specified.", violations.iterator().next().getMessage());
    }

    // Некорректный e-mail - ошибка
    @Test
    public void testInvalidEmailAddress() {
        UserCreateDto user = new UserCreateDto("Igor Plastinin", "mail: -(^v^)- )");

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals("The field must contain an email address.", violations.iterator().next().getMessage());
    }

    // Не задано имя пользователя и e-mail - ошибка
    @Test
    public void testMultipleViolations() {
        UserCreateDto user = new UserCreateDto("", "");

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        for (ConstraintViolation<?> violation : violations) {
            if ("Username must be specified.".equals(violation.getMessage())) {
                continue;
            } else if ("The field must contain an email address.".equals(violation.getMessage())) {
                continue;
            } else
                fail("Unexpected validation error message.");
        }
    }
}