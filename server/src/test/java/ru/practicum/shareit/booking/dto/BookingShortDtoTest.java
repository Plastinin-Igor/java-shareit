package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingShortDtoTest {

    @Autowired
    private JacksonTester<BookingShortDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        BookingShortDto bookingShortDto = new BookingShortDto(1L, LocalDateTime.now(), LocalDateTime.now(),
                1L, 1L);

        JsonContent<BookingShortDto> bookingDtoInputSaved = jacksonTester.write(bookingShortDto);

        assertThat(bookingDtoInputSaved).hasJsonPath("$.id");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.start");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.end");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.bookerId");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.itemId");

    }

}