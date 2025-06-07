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

}