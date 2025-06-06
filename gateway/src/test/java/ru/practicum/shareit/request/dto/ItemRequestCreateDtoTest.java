package ru.practicum.shareit.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestCreateDtoTest {
    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Описание не задано - ошибка
    @Test
    public void testInvalidWithoutDescription() {

        ItemRequestCreateDto invalidItem = new ItemRequestCreateDto("");

        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(invalidItem);

        assertFalse(violations.isEmpty());
        assertEquals("Request description must be specified", violations.iterator().next().getMessage());
    }

    // Описание задано
    @Test
    public void testValidWithoutDescription() {

        ItemRequestCreateDto invalidItem = new ItemRequestCreateDto("I really need a hammer drill!");

        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(invalidItem);

        assertTrue(violations.isEmpty());
    }

}