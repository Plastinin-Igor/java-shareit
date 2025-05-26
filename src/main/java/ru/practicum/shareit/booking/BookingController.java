package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                 @PositiveOrZero Long userId,
                                 @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Received a request to add a booking.");
        BookingDto bookingDto = bookingService.addBooking(bookingCreateDto, userId);
        log.info("Booking with id: {} added for user with id: {}.", bookingDto.getId(), userId);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public void approveBooking(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                               @PositiveOrZero Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) {
        log.info("Received a request to approve a booking.");
        bookingService.approveBooking(bookingId, userId, approved);
        log.info("Booking with id: {} approved", bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        log.info("Received a request to get a booking with id: {}.", bookingId);
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping()
    public Collection<BookingDto> getBookingByUser(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                   @PositiveOrZero Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Received a request to get a booking with userId: {} and state: {}.", userId, state);
        return bookingService.getBookingByUser(userId, state);
    }

    @GetMapping("owner")
    public Collection<BookingDto> getBookingItemByUser(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                       @PositiveOrZero Long userId,
                                                       @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Get a list of reservations for all items of a user with id: {} and state: {}.", userId, state);
        return bookingService.getBookingItemByUser(userId, state);
    }
}
