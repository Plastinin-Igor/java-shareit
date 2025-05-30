package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @NotNull
    private Long itemId;


    private Long bookerId;

    private BookingStatus status;

    @AssertTrue
    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
