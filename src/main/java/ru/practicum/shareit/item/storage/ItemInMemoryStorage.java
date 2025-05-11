package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
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
    public Item updateItem(Item item, Long userId) {
        Item itemOld = items.get(item.getId());
        if (!userId.equals(itemOld.getOwner().getId())) {
            log.error("Only the owner can make changes.");
            throw new IllegalArgumentException("Only the owner can make changes.");
        }
        itemOld.setName(item.getName());
        itemOld.setDescription(item.getDescription());
        itemOld.setAvailable(item.getAvailable());
        return item;
    }

    @Override
    public boolean deleteItem(Long itemId) {
        return (items.remove(itemId) != null);
    }

    @Override
    public Item getItemById(Long itemId, Long userId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getItems(Long userId) {
        return items.values().stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findItems(String searchText) {
        if (!searchText.isBlank()) {
            String textLower = searchText.toLowerCase();
            return items.values().stream()
                    .filter(i -> (i.getName().toLowerCase().contains(textLower) ||
                            i.getDescription().toLowerCase().contains(textLower)) &&
                            i.getAvailable())
                    .collect(Collectors.toList());
        } else
            return Collections.emptyList();
    }

}
