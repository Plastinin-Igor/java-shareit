package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
public class ItemInMemoryStorage implements ItemStorage {

    private final Map<Long, Item> items;
    private final Map<Long, Long> itemsUsers;

    public ItemInMemoryStorage() {
        items = new HashMap<>();
        itemsUsers = new HashMap<>();
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
        itemsUsers.put(item.getId(), item.getOwner().getId());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item itemOld = items.get(item.getId());
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
        if (!items.containsKey(itemId) && (itemsUsers.get(userId).equals(itemId))) {
            log.error("Item with id: {} not found for user with id: {}.", itemId, userId);
            throw new NotFoundException("Item with id: " + itemId + " not found for user with id: " + userId);
        }
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getItems(Long userId) {
        ArrayList<Item> itemsList = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : itemsUsers.entrySet()) {
            Long item = entry.getKey();
            Long user = entry.getValue();
            if (userId.equals(user)) {
                itemsList.add(items.get(item));
            }
        }
        return itemsList;
    }

    @Override
    public Collection<Item> findItems(String searchText) {
        ArrayList<Item> itemsList = new ArrayList<>();
        if (!searchText.isBlank()) {
            for (Item item : items.values()) {
                if (item.getName().toLowerCase().contains(searchText.toLowerCase())
                        || item.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    if (item.getAvailable().equals(true)) {
                        itemsList.add(item);
                    }
                }
            }
        }
        return itemsList;
    }
}
