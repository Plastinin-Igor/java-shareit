package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto addItem(ItemCreateDto itemCreateDto, Long userId) {
        User user = getUser(userId);
        Item item = ItemMapper.toItemFromCreateDto(itemCreateDto);
        item.setOwner(user);
        item = itemStorage.addItem(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long itemId, Long userId) {
        getUser(userId);
        Item oldItem = getItem(itemId, userId);
        Item newItem = ItemMapper.toItemFromUpdateDto(oldItem, itemUpdateDto);
        return ItemMapper.toItemDto(itemStorage.updateItem(newItem, userId));
    }

    @Override
    public boolean deleteItem(Long itemId, Long userId) {
        getItem(itemId, userId);
        return itemStorage.deleteItem(itemId);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        return ItemMapper.toItemDto(getItem(itemId, userId));
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        return itemStorage.getItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findItems(String searchText, Long userId) {
        return itemStorage.findItems(searchText).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item getItem(long itemId, long userId) {
        Item item = itemStorage.getItemById(itemId, userId);
        if (item == null) {
            log.error("Item with id: {} not found.", itemId);
            throw new NotFoundException("Item with id: " + itemId + " not found.");
        }
        return item;
    }

    private User getUser(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.error("User with id: {} not found.", userId);
            throw new NotFoundException("User with id: " + userId + " not found.");
        }
        return user;
    }
}
