package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                         Long userId,
                                         @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Received a request to add a ItemRequest.");
        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(itemRequestCreateDto, userId);
        log.info("ItemRequest with id: {} added for user with id: {}.", itemRequestDto.getId(), userId);
        return itemRequestDto;
    }

    @GetMapping
    Collection<ItemRequestDto> getListOfYourRequests(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                     Long userId) {
        log.info("Received a request to get a ItemRequest with user id: {}", userId);
        return itemRequestService.getListOfYourRequests(userId);
    }

    @GetMapping("/all")
    Collection<ItemRequestDto> getListQueriesCreatedByOtherUsers(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                                 Long userId) {
        log.info("Received a request to get a ItemRequest with users not id: {}", userId);
        return itemRequestService.getListQueriesCreatedByOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getItemRequestById(@PathVariable Long requestId) {
        log.info("Received a request to get a ItemRequest with id: {}", requestId);
        return itemRequestService.getItemRequestById(requestId);
    }
}
