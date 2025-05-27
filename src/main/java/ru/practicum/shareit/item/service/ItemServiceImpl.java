package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ItemDto addItem(ItemCreateDto itemCreateDto, Long userId) {
        User user = getUser(userId);
        Item item = ItemMapper.toItemFromCreateDto(itemCreateDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long itemId, Long userId) {
        getUser(userId);
        Item oldItem = getItem(itemId);
        if (!userId.equals(oldItem.getOwner().getId())) {
            log.error("Only the owner can make changes.");
            throw new IllegalArgumentException("Only the owner can make changes.");
        }
        Item newItem = ItemMapper.toItemFromUpdateDto(oldItem, itemUpdateDto);
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        Item item = getItem(itemId);
        if (!userId.equals(item.getOwner().getId())) {
            log.error("Only the owner can make delete item.");
            throw new IllegalArgumentException("Only the owner can make delete item.");
        }
        itemRepository.delete(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        return ItemMapper.toItemDto(getItem(itemId));
    }

    @Override
    public Collection<ItemWithBookingDto> getItems(Long userId) {
        User user = getUser(userId);

        // Все вещи пользователя
        List<ItemWithBookingDto> items;
        items = itemRepository.findByOwner_Id(userId).stream()
                .map(ItemMapper::toItemWithDateDto)
                .toList();

        // Все бронирования вещей пользователя
        List<BookingShortDto> bokings;
        bokings = bookingRepository.findByItem_Owner(user, Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .map(BookingMapper::toBookingShortDto)
                .toList();

        //Результат: вещь с информацией о последнем и следующем бронировании
        Optional<BookingShortDto> bookingLastShortDto;
        Optional<BookingShortDto> bookingNextShortDto;
        List<ItemWithBookingDto> results = new ArrayList<>();

        for (ItemWithBookingDto item : items) {
            bookingLastShortDto = bokings.stream()
                    .filter(b -> Objects.equals(b.getItemId(), item.getId())) //Условие на вещь
                    .filter(b -> !b.getStart().isAfter(LocalDateTime.now())) //Прошедшие бронирования
                    .max(Comparator.comparing(BookingShortDto::getStart));

            bookingNextShortDto = bokings.stream()
                    .filter(b -> Objects.equals(b.getItemId(), item.getId())) //Условие на вещь
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now())) //Будущие бронирования
                    .min(Comparator.comparing(BookingShortDto::getStart));

            bookingLastShortDto.ifPresent(item::setLastBooking);
            bookingNextShortDto.ifPresent(item::setNextBooking);

            results.add(item);
        }

        return results;
    }

    @Override
    public Collection<ItemDto> findItems(String searchText, Long userId) {
        if (!searchText.isBlank()) {
            return itemRepository.findByText(searchText.toLowerCase()).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else
            return Collections.emptyList();
    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id: " + itemId + " not found."));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found."));

    }

}
