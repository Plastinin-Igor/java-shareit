package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(Item item, Long userId);

    boolean deleteItem(Long itemId);

    Item getItemById(Long itemId, Long userId);

    Collection<Item> getItems(Long userId);

    Collection<Item> findItems(String searchText);

}
