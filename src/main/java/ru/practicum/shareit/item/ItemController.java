package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                           @PositiveOrZero Long userId,
                           @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Received a request to add a item.");
        ItemDto itemDto = itemService.addItem(itemCreateDto, userId);
        log.info("Item with id: {} added for user with id: {}.", itemDto.getId(), userId);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                              @PositiveOrZero Long userId,
                              @PathVariable Long itemId,
                              @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        log.info("Received a request to modify a item with id: {}.", itemId);
        ItemDto itemDto = itemService.updateItem(itemUpdateDto, itemId, userId);
        log.info("Item with id: {} modified.", itemId);
        return itemDto;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                           @PositiveOrZero Long userId,
                           @PathVariable Long itemId) {
        log.info("Received a request to delete item with id: {}.", itemId);
        itemService.deleteItem(itemId, userId);
        log.info("Item with id: {} deleted.", itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                               @PositiveOrZero Long userId,
                               @PathVariable Long itemId) {
        log.info("Received a request to get a item with id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                        @PositiveOrZero Long userId) {
        log.info("Received a request to get a items with user id: {}", userId);
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItems(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                         @PositiveOrZero Long userId,
                                         @RequestParam String text) {
        log.info("Received a request to get a items with text: {}", text);
        return itemService.findItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(name = "X-Sharer-User-Id", required = false, defaultValue = "-1")
                                 @PositiveOrZero Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentCreateDto commentCreateDto) {
        log.info("Received a request to add a comment");
        return itemService.addComment(itemId, userId, commentCreateDto);
    }

}
