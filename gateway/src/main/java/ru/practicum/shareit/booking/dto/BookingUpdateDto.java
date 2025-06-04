package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingUpdateDto {

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private Long item;

    private Long booker;

    private BookingStatus status;

    @AssertTrue
    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }

    public boolean hasStart() {
        return (start != null);
    }

    public boolean hasEnd() {
        return (end != null);
    }

    public boolean hasItem() {
        return (item != null);
    }

    public boolean hasBooker() {
        return (booker != null);
    }

    public boolean hasStatus() {
        return (start != null);
    }

}
