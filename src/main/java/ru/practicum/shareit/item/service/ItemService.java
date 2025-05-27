package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemCreateDto item, Long userId);

    ItemDto updateItem(ItemUpdateDto item, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    Collection<ItemWithBookingDto> getItems(Long userId);

    Collection<ItemDto> findItems(String searchText, Long userId);
}
