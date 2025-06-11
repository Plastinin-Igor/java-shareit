package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto addItemRequest(ItemRequestCreateDto itemRequestCreateDto, Long userId);

    // Получить список своих запросов
    Collection<ItemRequestDto> getListOfYourRequests(Long userId);

    // Получить список запросов, созданных другими пользователями
    Collection<ItemRequestDto> getListQueriesCreatedByOtherUsers(Long userId);

    // Получить данные об одном конкретном запросе
    ItemRequestDto getItemRequestById(Long itemRequestId);


}
