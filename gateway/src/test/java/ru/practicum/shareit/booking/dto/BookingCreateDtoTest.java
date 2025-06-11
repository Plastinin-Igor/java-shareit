package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingCreateDtoTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //Начало бронирования в прошлом - ошибка
    @Test
    void whenStartIsInPastThenValidationFails() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5), 1L, 2L, BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }

    // Бронирование с корректным временем начала и конца
    @Test
    void whenStartIsValidAndEndIsInTheFutureThenValidationSucceeds() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(start, end, 1L, 2L, BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(bookingCreateDto);

        assertTrue(violations.isEmpty());
    }

    // Начальная и конечная даты равны - ошибка
    @Test
    void whenStartIsEqualToEndThenValidationFails() {
        LocalDateTime dateTime = LocalDateTime.now();
        BookingCreateDto bookingCreateDto = new BookingCreateDto(dateTime, dateTime, 1L, 2L,
                BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }

    // id null - ошибка
    @Test
    void whenItemIdIsNullThenValidationFails() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                null, 2L, BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }

}