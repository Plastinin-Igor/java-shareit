package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(ItemRequestCreateDto itemRequestCreateDto, Long userId) {
        User user = getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequestFromCreateDto(itemRequestCreateDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    // Получить список своих запросов
    @Override
    public Collection<ItemRequestDto> getListOfYourRequests(Long userId) {
        getUser(userId);
        // Список своих запросов
        List<ItemRequestDto> itemRequestList = itemRequestRepository
                .getItemRequestByRequestor_IdOrderByCreatedAsc(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();

        // Список всех вещей, у которых есть запросы
        List<Item> itemList = itemRepository.findAllByRequest_IdNotNull();
        List<ItemShortDto> itemShortDto;

        for (ItemRequestDto itemRequest : itemRequestList) {
            itemShortDto = itemList.stream()
                    .filter(i -> Objects.equals(i.getRequest().getId(), itemRequest.getId()))
                    .map(ItemMapper::toItemShortDtoFromItem)
                    .toList();
            if (!itemShortDto.isEmpty()) {
                itemRequest.setItems(itemShortDto);
            }
        }

        return itemRequestList;
    }

    // Получить список запросов, созданных другими пользователями
    @Override
    public Collection<ItemRequestDto> getListQueriesCreatedByOtherUsers(Long userId) {
        getUser(userId);
        // Список запросов, созданных другими пользователями
        List<ItemRequestDto> itemRequestList = itemRequestRepository
                .findAll()
                .stream()
                .filter(i -> !Objects.equals(i.getRequestor().getId(), userId)) // Свои запросы исключаем
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();

        // Список всех вещей, у которых есть запросы
        List<Item> itemList = itemRepository.findAllByRequest_IdNotNull();
        List<ItemShortDto> itemShortDto;

        for (ItemRequestDto itemRequest : itemRequestList) {
            itemShortDto = itemList.stream()
                    .filter(i -> Objects.equals(i.getRequest().getId(), itemRequest.getId()))
                    .map(ItemMapper::toItemShortDtoFromItem)
                    .toList();
            if (!itemShortDto.isEmpty()) {
                itemRequest.setItems(itemShortDto);
            }
        }

        return itemRequestList;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId) {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest with id: " + itemRequestId + " not found.")));

        List<ItemShortDto> itemList = itemRepository.findAllByRequest_Id(itemRequestId)
                .stream()
                .map(ItemMapper::toItemShortDtoFromItem)
                .toList();

        itemRequestDto.setItems(itemList);

        return itemRequestDto;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found."));

    }

}
