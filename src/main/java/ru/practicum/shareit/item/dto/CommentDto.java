package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    private String text;

    private ItemDto item;

    private UserDto author;

    private String authorName;

    private LocalDateTime created;
}