package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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

}