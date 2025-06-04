package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

public final class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner() != null ? UserMapper.toUserDto(item.getOwner()) : null);
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(itemDto.getOwner() != null ? UserMapper.toUser(itemDto.getOwner()) : null);
        return item;
    }

    public static Item toItemFromCreateDto(ItemCreateDto itemCreateDto) {
        Item item = new Item();
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getAvailable());

        if (itemCreateDto.getRequestId() != null) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setId(itemCreateDto.getRequestId());
            item.setRequest(itemRequest);
        } else {
            item.setRequest(null);
        }

        return item;
    }

    public static Item toItemFromUpdateDto(Item item, ItemUpdateDto itemUpdateDto) {
        if (itemUpdateDto.hasName()) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.hasDescription()) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        item.setAvailable(itemUpdateDto.getAvailable());

        return item;
    }

    public static ItemShortDto toItemShortDtoFromItem(Item item) {
        ItemShortDto itemShortDto = new ItemShortDto();
        itemShortDto.setId(item.getId());
        itemShortDto.setName(item.getName());
        itemShortDto.setOwner(item.getOwner().getId());
        return itemShortDto;
    }

}
