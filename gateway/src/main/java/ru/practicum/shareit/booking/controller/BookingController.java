package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                             @PositiveOrZero Long userId,
                                             @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Received a request to add a booking.");
        return bookingClient.bookItem(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                 @PositiveOrZero Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam Boolean approved) {
        log.info("Received a request to approve a booking.");
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                 @PositiveOrZero Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Received a request to get a booking with id: {}.", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookingByUser(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                   @PositiveOrZero Long userId,
                                                   @RequestParam(defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Received a request to get a booking with userId: {} and state: {}.", userId, state);
        return bookingClient.getBookingByUser(userId, state);
    }

    @GetMapping("owner")
    public ResponseEntity<Object> getBookingItemByUser(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                       @PositiveOrZero Long userId,
                                                       @RequestParam(defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get a list of reservations for all items of a user with id: {} and state: {}.", userId, state);
        return bookingClient.getBookingItemByUser(userId, state);
    }
}
