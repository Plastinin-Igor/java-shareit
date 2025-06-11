package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {
    @NotBlank(message = "Item name must be specified")
    private String name;

    @NotBlank(message = "Item description must be specified")
    private String description;

    @NotNull(message = "Field available must be specified")
    private Boolean available;

    private Long owner;
    private Long requestId;
}
