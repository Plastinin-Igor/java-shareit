package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(Item item);

    boolean deleteItem(long itemId);

    Item getItemById(long itemId);

    Collection<Item> getItems();

    Collection<Item> findItems(String searchText);

}
