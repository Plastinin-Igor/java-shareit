package ru.practicum.shareit.comment.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
public class CommentCreateDto {

    private String text;
    private ItemDto item;
    private UserDto author;
}
