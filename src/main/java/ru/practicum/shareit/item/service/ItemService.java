package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemCreateDto item, Long userId);

    ItemDto updateItem(ItemUpdateDto item, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    Collection<ItemDto> getItems(Long userId);

    Collection<ItemDto> findItems(String searchText, Long userId);

    CommentDto addComment(Long itemId, Long userId, CommentCreateDto commentCreateDto);
}
