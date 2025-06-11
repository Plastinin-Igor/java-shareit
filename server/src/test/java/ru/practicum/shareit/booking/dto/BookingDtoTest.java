package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now(),
                new ItemDto(), new UserDto(), BookingStatus.WAITING);

        JsonContent<BookingDto> bookingDtoInputSaved = jacksonTester.write(bookingDto);

        assertThat(bookingDtoInputSaved).hasJsonPath("$.id");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.start");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.end");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.booker");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.item");
        assertThat(bookingDtoInputSaved).hasJsonPath("$.status");

    }

    @Test
    void testDeserialize() throws Exception {
        String json = String.format("{" +
                        "  \"id\": 1," +
                        "  \"start\": \"%s\"," +
                        "  \"end\": \"%s\"," +
                        "  \"item\": {},\n" +
                        "  \"booker\": {},\n" +
                        "  \"status\": \"WAITING\"" +
                        "}",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        BookingDto deserializedBookingDto = jacksonTester.parseObject(json);

        assertThat(deserializedBookingDto.getId()).isEqualTo(1L);
        assertThat(deserializedBookingDto.getStart()).isNotNull();
        assertThat(deserializedBookingDto.getEnd()).isNotNull();
        assertThat(deserializedBookingDto.getItem()).isNotNull();
        assertThat(deserializedBookingDto.getBooker()).isNotNull();
        assertThat(deserializedBookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

}