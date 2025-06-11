package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
class BookingCreateDtoTest {

    @Autowired
    private JacksonTester<BookingCreateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now(), LocalDateTime.now(),
                1L, 1L, BookingStatus.WAITING);

        JsonContent<BookingCreateDto> bookingDtoInputSaved = jacksonTester.write(bookingCreateDto);

        assertThat(bookingDtoInputSaved).hasJsonPath("$.start");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.end");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.bookerId");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.itemId");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.status");

    }

    @Test
    void testIsStartBeforeEnd_StartBeforeEnd_ReturnsTrue() {
        BookingCreateDto booking = new BookingCreateDto(
                LocalDateTime.of(2025, 1, 1, 12, 0), // Start: 1 Jan 2025, noon
                LocalDateTime.of(2025, 2, 1, 12, 0), // End: 1 Feb 2025, noon
                1L,
                1L,
                null
        );

        assertTrue(booking.isStartBeforeEnd());
    }

    @Test
    void testIsStartBeforeEnd_StartEqualsEnd_ReturnsFalse() {
        BookingCreateDto booking = new BookingCreateDto(
                LocalDateTime.of(2025, 1, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                1L,
                1L,
                null
        );

        // Check that the method returns false since start and end are equal
        assertFalse(booking.isStartBeforeEnd());
    }

    @Test
    void testIsStartBeforeEnd_StartAfterEnd_ReturnsFalse() {
        BookingCreateDto booking = new BookingCreateDto(
                LocalDateTime.of(2025, 2, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                1L,
                1L,
                null
        );

        assertFalse(booking.isStartBeforeEnd());
    }

}