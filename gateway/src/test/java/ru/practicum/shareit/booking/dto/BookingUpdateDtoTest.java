package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingUpdateDtoTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //Начало бронирования в прошлом - ошибка
    @Test
    void whenStartIsInPastThenValidationFails() {
        BookingUpdateDto bookingCreateDto = new BookingUpdateDto(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5), 1L, 2L, BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingUpdateDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }

    // Бронирование с корректным временем начала и конца
    @Test
    void whenStartIsValidAndEndIsInTheFutureThenValidationSucceeds() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        BookingUpdateDto bookingCreateDto = new BookingUpdateDto(start, end, 1L, 2L, BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingUpdateDto>> violations = validator.validate(bookingCreateDto);

        assertTrue(violations.isEmpty());
    }

    // Начальная и конечная даты равны - ошибка
    @Test
    void whenStartIsEqualToEndThenValidationFails() {
        LocalDateTime dateTime = LocalDateTime.now();
        BookingUpdateDto bookingCreateDto = new BookingUpdateDto(dateTime, dateTime, 1L, 2L,
                BookingStatus.APPROVED);

        Set<ConstraintViolation<BookingUpdateDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }

}