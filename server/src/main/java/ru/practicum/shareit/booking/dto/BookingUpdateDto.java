package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private BookingStatus status;

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
