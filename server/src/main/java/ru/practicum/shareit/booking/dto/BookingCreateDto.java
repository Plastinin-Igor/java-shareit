package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;

    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
