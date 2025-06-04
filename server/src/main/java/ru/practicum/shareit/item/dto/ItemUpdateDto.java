package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasOwner() {
        return !(owner == null);
    }

    public boolean hasRequest() {
        return !(request == null);
    }
}
