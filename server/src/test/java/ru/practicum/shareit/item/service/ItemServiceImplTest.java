package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @Spy
    private Item item;

    @Spy
    private User user;

    @Spy
    private ItemMapper itemMapper;

    @Spy
    private ItemCreateDto itemCreateDto;

    @Spy
    private ItemDto itemDto;

    @Spy
    private ItemUpdateDto itemUpdateDto;

    @Spy
    private UserDto userDto;

    @Spy
    private BookingMapper bookingMapper;

    @Spy
    private CommentMapper commentMapper;

    @Spy
    Comment comment;

    @Spy
    CommentDto commentDto;

    @Spy
    CommentCreateDto commentCreateDto;


    @BeforeEach
    void setUp() {

        user = new User(1L, "Plastinin Igor", "plastinin@ya.ru");
        userDto = new UserDto(1L, "Plastinin Igor", "plastinin@ya.ru");
        item = new Item(1L, "Drill", "Drill tool", true, user, null);
        itemCreateDto = new ItemCreateDto("Drill", "Drill tool", true, 1L, null);
        itemUpdateDto = new ItemUpdateDto("Drill", "Drill tool", true, 1L, null);
        itemDto = new ItemDto(1L, "Drill", "Drill tool", true, userDto, null, null, null, List.of());
        comment = new Comment(1L, "Best tool", item, user, LocalDateTime.now());
        commentDto = new CommentDto(1L, 1L, "Best tool", "Plastinin Igor", LocalDateTime.now());
        commentCreateDto = new CommentCreateDto("Best tool", itemDto, userDto);
    }

    @Test
    void testAddItemSuccess() {

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user)); // моковое поведение для загрузки пользователя
        given(itemRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0)); // возвращаем сохраненный объект

        ItemDto result = itemService.addItem(itemCreateDto, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(itemDto.getName());
    }

    @Test
    void testAddItemUserNotFound() {

        Long invalidUserId = 999L;
        ItemCreateDto createDto = new ItemCreateDto();
        createDto.setName("test-item");

        given(userRepository.findById(invalidUserId)).willReturn(java.util.Optional.empty()); // возвращаем пустую Optional

        Throwable exception = catchThrowable(() -> itemService.addItem(createDto, invalidUserId));
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id: 999 not found"); // ожидаем исключение
    }

    @Test
    void testAddItemWithValidOwner() {
        // given
        Long validUserId = 1L;
        User owner = new User(validUserId, "test-user", "valid@mail.ru");
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("new-item");

        given(userRepository.findById(validUserId)).willReturn(Optional.of(owner));
        given(itemRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ItemDto savedItem = itemService.addItem(dto, validUserId);
        assertThat(savedItem.getOwner().getId()).isEqualTo(validUserId);
    }

    @Test
    void testSuccessfulUpdate() {

        Long userId = 1L;
        Long itemId = 1L;
        User owner = new User();
        owner.setId(userId);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setOwner(owner);
        existingItem.setName("old-name");

        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("updated-name");

        given(userRepository.findById(userId)).willReturn(Optional.of(owner));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(existingItem));
        given(itemRepository.save(any(Item.class))).willAnswer(i -> i.getArguments()[0]);

        ItemDto updatedItem = itemService.updateItem(updateDto, itemId, userId);
        assertThat(updatedItem.getName()).isEqualTo("updated-name");
    }

    @Test
    void testUpdateItemForbidden() {

        Long otherUserId = 99L;
        Long itemId = 1L;

        given(userRepository.findById(otherUserId)).willReturn(Optional.of(user));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));


        Throwable exception = catchThrowable(() -> itemService.updateItem(itemUpdateDto, itemId, otherUserId));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only the owner can make changes.");
    }

    @Test
    void testItemDoesNotExist() {

        Long nonExistingItemId = 999L;
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(itemRepository.findById(nonExistingItemId)).willReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> itemService.updateItem(null, nonExistingItemId, userId));
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Item with id: 999 not found.");
    }

    @Test
    public void testDeleteItemSuccessfully() {

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        itemService.deleteItem(item.getId(), item.getOwner().getId());

        verify(itemRepository).delete(item);
    }

    @Test
    public void testDeleteItemUnauthorized() {

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.deleteItem(item.getId(), 44L);
        });

        Assertions.assertEquals("Only the owner can make delete item.", exception.getMessage());
        verify(itemRepository, never()).delete(any());
    }

    @Test
    public void shouldGetItemWithComments() {
        // given
        Long itemId = 1L;
        Long userId = 1L;

        given(itemRepository.findById(itemId))
                .willReturn(Optional.of(item));


        given(commentRepository.findByItem_IdOrderByCreatedDesc(itemId))
                .willReturn(List.of(comment));

        ItemDto actualResult = itemService.getItemById(itemId, userId);

        assertThat(actualResult.getName()).isEqualTo("Drill");
        assertThat(actualResult.getComments()).containsExactlyInAnyOrder(
                CommentMapper.toCommentDto(comment)
        );
    }

    @Test
    public void shouldGetItemWithoutComments() {

        Long itemId = 1L;
        Long userId = 1L;

        given(itemRepository.findById(itemId))
                .willReturn(Optional.of(item));

        given(commentRepository.findByItem_IdOrderByCreatedDesc(itemId))
                .willReturn(List.of());

        ItemDto actualResult = itemService.getItemById(itemId, userId);

        assertThat(actualResult.getName()).isEqualTo("Drill");
        assertNull(actualResult.getComments());
    }

    /**
     * Тестируем успешный поиск элементов по тексту.
     */
    @Test
    public void testFindItemsWithSearchText() {

        String searchText = "drill";
        List<Item> items = List.of(new Item(), new Item());
        given(itemRepository.findByText(searchText.toLowerCase()))
                .willReturn(items);

        Collection<ItemDto> result = itemService.findItems(searchText, 1L);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindItemsWithEmptySearchText() {

        String searchText = "";

        Collection<ItemDto> result = itemService.findItems(searchText, 1L);

        assertThat(result).isEmpty();
    }

    @Test
    public void testFindItemsIgnoreCase() {

        String searchText = "DRILL";
        List<Item> items = List.of(new Item(), new Item());
        given(itemRepository.findByText(searchText.toLowerCase()))
                .willReturn(items);

        Collection<ItemDto> result = itemService.findItems(searchText, 1L);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindItemsWithWhitespace() {

        String searchText = " ";

        Collection<ItemDto> result = itemService.findItems(searchText, 1L);

        assertThat(result).isEmpty();
    }


    @Test
    public void testAddComment_Successful() {

        long itemId = 1L;
        long userId = 1L;
        String text = "Best tool";

        given(itemRepository.findById(eq(itemId)))
                .willReturn(java.util.Optional.of(item));
        given(userRepository.findById(eq(userId)))
                .willReturn(java.util.Optional.of(user));
        given(bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(eq(userId), eq(itemId), any(), any()))
                .willReturn(Collections.singletonList(new Booking()));

        given(commentRepository.save(any(Comment.class)))
                .willAnswer(i -> i.getArgument(0));

        CommentDto result = itemService.addComment(itemId, userId, commentCreateDto);

        assertThat(result.getText()).isEqualTo(text);
        assertThat(result.getAuthorName()).isEqualTo(user.getName());
    }

    @Test
    public void testAddComment_AccessDenied() {

        long itemId = 1L;
        long userId = 1L;

        given(itemRepository.findById(eq(itemId)))
                .willReturn(java.util.Optional.of(item));
        given(userRepository.findById(eq(userId)))
                .willReturn(java.util.Optional.of(user));
        given(bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(eq(userId), eq(itemId), any(), any()))
                .willReturn(Collections.emptyList()); // Аренда не найдена

        // When & Then
        assertThatThrownBy(() -> itemService.addComment(itemId, userId, commentCreateDto))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Access denied. The user did not rent the item.");
    }

}