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
class BookingUpdateDtoTest {

    @Autowired
    private JacksonTester<BookingUpdateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        BookingUpdateDto bookingUpdateDto = new BookingUpdateDto(LocalDateTime.now(), LocalDateTime.now(),
                1L, 1L, BookingStatus.WAITING);

        JsonContent<BookingUpdateDto> bookingDtoInputSaved = jacksonTester.write(bookingUpdateDto);
        assertThat(bookingDtoInputSaved).hasJsonPath("$.start");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.end");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.booker");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.item");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.status");

    }

    @Test
    void testIsStartBeforeEnd_StartBeforeEnd_ReturnsTrue() {
        BookingUpdateDto booking = new BookingUpdateDto(
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
        BookingUpdateDto booking = new BookingUpdateDto(
                LocalDateTime.of(2025, 1, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                1L,
                1L,
                null
        );

        assertFalse(booking.isStartBeforeEnd());
    }

    @Test
    void testIsStartBeforeEnd_StartAfterEnd_ReturnsFalse() {
        BookingUpdateDto booking = new BookingUpdateDto(
                LocalDateTime.of(2025, 2, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                1L,
                1L,
                null
        );

        assertFalse(booking.isStartBeforeEnd());
    }

    @Test
    void testHasStart_WithValidStart_ReturnsTrue() {

        BookingUpdateDto booking = new BookingUpdateDto(
                LocalDateTime.of(2025, 1, 1, 12, 0), // Начало: 1 января 2025, полдень
                null, // Без конечной даты
                1L, // item
                1L, // booker
                BookingStatus.WAITING // status
        );

        assertTrue(booking.hasStart());
    }

    @Test
    void testHasStart_WithoutStart_ReturnsFalse() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null, // Нет начальной даты
                LocalDateTime.of(2025, 1, 1, 12, 0), // Есть конечная дата
                1L, // item
                1L, // booker
                BookingStatus.WAITING // status
        );

        assertFalse(booking.hasStart());
    }

    @Test
    void testHasEnd_WithValidEnd_ReturnsTrue() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                LocalDateTime.of(2025, 1, 1, 12, 0),
                1L,
                1L,
                BookingStatus.WAITING
        );

        assertTrue(booking.hasEnd());
    }

    @Test
    void testHasEnd_WithoutEnd_ReturnsFalse() {

        BookingUpdateDto booking = new BookingUpdateDto(
                LocalDateTime.of(2025, 1, 1, 12, 0),
                null,
                1L,
                1L,
                BookingStatus.WAITING
        );

        assertFalse(booking.hasEnd());
    }

    @Test
    void testHasItem_WithValidItem_ReturnsTrue() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                null,
                1L,
                1L,
                BookingStatus.WAITING
        );

        assertTrue(booking.hasItem());
    }

    @Test
    void testHasItem_WithoutItem_ReturnsFalse() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                null,
                null,
                1L,
                BookingStatus.WAITING
        );

        assertFalse(booking.hasItem());
    }

    @Test
    void testHasBooker_WithValidBooker_ReturnsTrue() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                null,
                1L,
                1L,
                BookingStatus.WAITING
        );

        assertTrue(booking.hasBooker());
    }

    @Test
    void testHasBooker_WithoutBooker_ReturnsFalse() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                null,
                1L,
                null,
                BookingStatus.WAITING
        );

        assertFalse(booking.hasBooker());
    }

    @Test
    void testHasStatus_WithoutStatus_ReturnsFalse() {

        BookingUpdateDto booking = new BookingUpdateDto(
                null,
                null,
                1L,
                1L,
                null
        );

        assertFalse(booking.hasStatus());
    }

}