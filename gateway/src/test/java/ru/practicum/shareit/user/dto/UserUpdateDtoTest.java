package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDtoTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Некорректный e-mail - ошибка
    @Test
    public void testInvalidEmailAddress() {
        UserCreateDto user = new UserCreateDto("Igor Plastinin", "mail: -(^v^)- )");

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals("The field must contain an email address.", violations.iterator().next().getMessage());
    }

}