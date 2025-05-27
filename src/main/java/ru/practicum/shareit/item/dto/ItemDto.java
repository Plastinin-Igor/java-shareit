package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private ItemRequestDto request;
    private List<CommentDto> comments;
}
