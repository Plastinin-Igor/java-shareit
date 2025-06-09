package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class BookingMapperTest {

    @Test
    public void testToBooking_MappingFieldsCorrectly() {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2023, 10, 1, 10, 0));
        bookingDto.setEnd(LocalDateTime.of(2023, 10, 1, 12, 0));
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());
        bookingDto.setStatus(BookingStatus.APPROVED);

        Booking booking = BookingMapper.toBooking(bookingDto);

        assertThat(booking.getId()).isEqualTo(bookingDto.getId());
        assertThat(booking.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(booking.getEnd()).isEqualTo(bookingDto.getEnd());
        assertThat(booking.getItem().getId()).isEqualTo(bookingDto.getItem().getId());
        assertThat(booking.getBooker().getId()).isEqualTo(bookingDto.getBooker().getId());
        assertThat(booking.getStatus()).isEqualTo(bookingDto.getStatus());
    }

    @Test
    public void testToBookingShortDto() {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2023, 10, 1, 10, 0));
        booking.setEnd(LocalDateTime.of(2023, 10, 1, 12, 0));

        Item item = new Item();
        item.setId(10L);
        booking.setItem(item);

        User booker = new User();
        booker.setId(20L);
        booking.setBooker(booker);

        BookingShortDto bookingShortDto = BookingMapper.toBookingShortDto(booking);

        assertThat(bookingShortDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingShortDto.getStart()).isEqualTo(booking.getStart());
        assertThat(bookingShortDto.getEnd()).isEqualTo(booking.getEnd());
        assertThat(bookingShortDto.getItemId()).isEqualTo(booking.getItem().getId());
        assertThat(bookingShortDto.getBookerId()).isEqualTo(booking.getBooker().getId());
    }

}