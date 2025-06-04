package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                 @PositiveOrZero Long userId,
                                                 @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Received a request to add a ItemRequest.");
        return itemRequestClient.addItemRequest(itemRequestCreateDto, userId);
    }

    @GetMapping
    ResponseEntity<Object> getListOfYourRequests(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                 @PositiveOrZero Long userId) {
        log.info("Received a request to get a ItemRequest with user id: {}", userId);
        return itemRequestClient.getListOfYourRequests(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getListQueriesCreatedByOtherUsers(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                                             @PositiveOrZero Long userId) {
        log.info("Received a request to get a ItemRequest with users not id: {}", userId);
        return itemRequestClient.getListQueriesCreatedByOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId) {
        log.info("Received a request to get a ItemRequest with id: {}", requestId);
        return itemRequestClient.getItemRequestById(requestId);
    }
}
