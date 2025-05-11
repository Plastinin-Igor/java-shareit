package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ItemRequestCreateDto {
    @NotBlank(message = "Request description must be specified")
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
