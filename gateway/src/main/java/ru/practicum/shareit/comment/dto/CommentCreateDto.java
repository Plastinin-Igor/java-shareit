package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    @NotNull(message = "The text of the comment must be specified")
    @NotBlank(message = "The text of the comment must be specified")
    private String text;

    @NotNull(message = "Item description must be specified")
    private ItemDto item;

    @NotNull(message = "Author must be specified")
    private UserDto author;
}
