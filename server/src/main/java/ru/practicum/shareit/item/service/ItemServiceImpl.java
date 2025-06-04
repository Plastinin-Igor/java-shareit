package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;


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
        ItemDto itemDto = ItemMapper.toItemDto(getItem(itemId));

        List<CommentDto> commentDtoList;
        commentDtoList = commentRepository.findByItem_IdOrderByCreatedDesc(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        if (!commentDtoList.isEmpty()) {
            itemDto.setComments(commentDtoList);
        }

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        User user = getUser(userId);

        // Все вещи пользователя
        List<ItemDto> items;
        items = itemRepository.findByOwner_Id(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();

        // Все бронирования вещей пользователя
        List<BookingShortDto> bookings;
        bookings = bookingRepository.findByItem_Owner(user, Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .map(BookingMapper::toBookingShortDto)
                .toList();

        // Все комментарии
        List<CommentDto> comments;
        comments = commentRepository.findAll()
                .stream()
                .map(CommentMapper::toCommentDto)
                .toList();

        //Результат: вещь с информацией о последнем, следующем бронировании и комментариями
        Optional<BookingShortDto> bookingLastShortDto;
        Optional<BookingShortDto> bookingNextShortDto;
        List<CommentDto> commentsItem;
        List<ItemDto> results = new ArrayList<>();

        for (ItemDto item : items) {
            bookingLastShortDto = bookings.stream()
                    .filter(b -> Objects.equals(b.getItemId(), item.getId())) //Условие на вещь
                    .filter(b -> !b.getStart().isAfter(LocalDateTime.now())) //Прошедшие бронирования
                    .max(Comparator.comparing(BookingShortDto::getStart));

            bookingNextShortDto = bookings.stream()
                    .filter(b -> Objects.equals(b.getItemId(), item.getId())) //Условие на вещь
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now())) //Будущие бронирования
                    .min(Comparator.comparing(BookingShortDto::getStart));

            commentsItem = comments.stream()
                    .filter(i -> Objects.equals(i.getItem(), item.getId()))
                    .toList();


            bookingLastShortDto.ifPresent(item::setLastBooking);
            bookingNextShortDto.ifPresent(item::setNextBooking);
            if (!commentsItem.isEmpty()) {
                item.setComments(commentsItem);
            }
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

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long userId, CommentCreateDto commentCreateDto) {
        Item item = getItem(itemId);
        User user = getUser(userId);

        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId, itemId,
                BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            log.error("Access denied. The user did not rent the item.");
            throw new ValidationException("Access denied. The user did not rent the item.");
        }

        Comment comment = CommentMapper.toCommentFromCreateDto(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        CommentDto commentDto = CommentMapper.toCommentDto(commentRepository.save(comment));
        commentDto.setAuthorName(user.getName());
        return commentDto;
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
