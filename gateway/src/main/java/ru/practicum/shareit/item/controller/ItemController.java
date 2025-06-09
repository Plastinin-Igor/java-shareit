package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collections;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                          @PositiveOrZero Long userId,
                                          @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Received a request to add a item.");
        return itemClient.addItem(itemCreateDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                             @PositiveOrZero Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        log.info("Received a request to modify a item with id: {}.", itemId);
        return itemClient.updateItem(itemUpdateDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                           @PositiveOrZero Long userId,
                           @PathVariable Long itemId) {
        log.info("Received a request to delete item with id: {}.", itemId);
        itemClient.deleteItem(itemId, userId);
        log.info("Item with id: {} deleted.", itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                              @PositiveOrZero Long userId,
                                              @PathVariable Long itemId) {
        log.info("Received a request to get a item with id: {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                           @PositiveOrZero Long userId) {
        log.info("Received a request to get a items with user id: {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                            @PositiveOrZero Long userId,
                                            @RequestParam String text) {
        log.info("Received a request to get a items with text: {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            return itemClient.findItems(text, userId);
        }
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                             @PositiveOrZero Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentCreateDto commentCreateDto) {
        log.info("Received a request to add a comment");
        return itemClient.addComment(itemId, userId, commentCreateDto);
    }

}
