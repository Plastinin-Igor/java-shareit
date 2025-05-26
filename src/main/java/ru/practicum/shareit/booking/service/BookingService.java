package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(BookingCreateDto booking, Long userId);

    BookingDto updateBooking(BookingUpdateDto booking, Long bookingId, Long userId);

    void deleteBooking(Long bookingId, Long userId);

    void approveBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId);

    Collection<BookingDto> getBookingByUser(Long userId, BookingState state);

    Collection<BookingDto> getBookingItemByUser(Long userId, BookingState state);

}
