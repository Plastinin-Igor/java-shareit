package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ItemCreateDto {
    @NotBlank(message = "Item name must be specified")
    private String name;
    @NotBlank(message = "Item description must be specified")
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;
}
