package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

public class ItemRequestUpdateDto {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
