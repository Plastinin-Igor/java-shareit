package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public final class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem() != null ? ItemMapper.toItemDto(booking.getItem()) : null);
        bookingDto.setBooker(booking.getBooker() != null ? UserMapper.toUserDto(booking.getBooker()) : null);
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(bookingDto.getItem() != null ? ItemMapper.toItem(bookingDto.getItem()) : null);
        booking.setBooker(bookingDto.getBooker() != null ? UserMapper.toUser(bookingDto.getBooker()) : null);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static Booking toBookingFromCreateDto(BookingCreateDto bookingCreateDto) {
        Booking booking = new Booking();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setStatus(bookingCreateDto.getStatus());
        return booking;
    }

    public static Booking toBookingFromUpdateDto(Booking booking, BookingUpdateDto bookingUpdateDto) {
        if (bookingUpdateDto.hasStart()) {
            booking.setStart(bookingUpdateDto.getStart());
        }

        if (bookingUpdateDto.hasEnd()) {
            booking.setEnd(bookingUpdateDto.getEnd());
        }

        if (bookingUpdateDto.hasStatus()) {
            booking.setStatus(bookingUpdateDto.getStatus());
        }

        return booking;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        BookingShortDto bookingShortDto = new BookingShortDto();
        bookingShortDto.setId(booking.getId());
        bookingShortDto.setStart(booking.getStart());
        bookingShortDto.setEnd(booking.getEnd());
        bookingShortDto.setItemId(booking.getItem().getId());
        bookingShortDto.setBookerId(booking.getBooker().getId());

        return bookingShortDto;
    }

}
