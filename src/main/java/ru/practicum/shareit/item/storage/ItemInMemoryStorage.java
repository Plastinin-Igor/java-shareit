package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import lombok.extern.log4j.Log4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Log4j
public class ItemInMemoryStorage implements ItemStorage {

    private final Map<Long, Item> items;

    public ItemInMemoryStorage() {
        items = new HashMap<>();
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }

    @Override
    public boolean deleteItem(long itemId) {
        return false;
    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public Collection<Item> getItems() {
        return List.of();
    }

    @Override
    public Collection<Item> findItems(String searchText) {
        return List.of();
    }
}
