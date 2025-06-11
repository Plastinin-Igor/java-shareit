package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemCreateDtoTest {


    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Правильная структура объекта
    @Test
    public void testValidItem() {
        ItemCreateDto validItem = new ItemCreateDto("Drill", "Impact drill",
                true, 1L, 1L);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(validItem);

        assertTrue(violations.isEmpty());
    }

    // Наименование не задано - ошибка
    @Test
    public void testInvalidWithoutName() {

        ItemCreateDto invalidItem = new ItemCreateDto("", "Impact drill",
                true, 1L, 1L);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(invalidItem);

        assertFalse(violations.isEmpty());
        assertEquals("Item name must be specified", violations.iterator().next().getMessage());
    }

    // Описание не задано - ошибка
    @Test
    public void testInvalidWithoutDescription() {

        ItemCreateDto invalidItem = new ItemCreateDto("Drill", "", true, 1L, 1L);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(invalidItem);

        assertFalse(violations.isEmpty());
        assertEquals("Item description must be specified", violations.iterator().next().getMessage());
    }

    // Доступность элемента не задана - ошибка
    @Test
    public void testInvalidWithoutAvailable() {

        ItemCreateDto invalidItem = new ItemCreateDto("Drill", "Impact drill",
                null, 1L, 1L);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(invalidItem);

        assertFalse(violations.isEmpty());
        assertEquals("Field available must be specified", violations.iterator().next().getMessage());
    }

    // Название и описание отсутствуют - ошибка
    @Test
    public void testMultipleViolations() {

        ItemCreateDto invalidItem = new ItemCreateDto("", "", true, 1L, 1L);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(invalidItem);

        assertEquals(2, violations.size());
        for (ConstraintViolation<?> violation : violations) {
            if ("Item name must be specified".equals(violation.getMessage())) {
                continue;
            } else if ("Item description must be specified".equals(violation.getMessage())) {
                continue;
            } else
                fail("Unexpected validation error message");
        }
    }

}