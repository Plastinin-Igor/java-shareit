package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUpdateDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ItemRequestMapper requestMapper;

    @Spy
    private ItemMapper itemMapper;

    private ItemRequest itemRequest;
    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestUpdateDto itemRequestUpdateDto;
    private ItemRequestDto itemRequestDto;
    private User user;
    private UserDto userDto;
    private Item item;
    private ItemDto itemDto;
    private ItemShortDto itemShortDto;
    private User currentUser;
    private User anotherUser;
    private ItemRequest firstRequest;
    private ItemRequest secondRequest;
    private Item firstItem;
    private Item secondItem;


    @BeforeEach
    void setUp() {
        user = new User(1L, "Plastinin Igor", "plastinin@ya.ru");
        userDto = new UserDto(1L, "Plastinin Igor", "plastinin@ya.ru");
        itemRequest = new ItemRequest(1L, "Need a drill", user, LocalDateTime.now());
        itemRequestCreateDto = new ItemRequestCreateDto("Need a drill");
        itemRequestUpdateDto = new ItemRequestUpdateDto(1L, "Need a drill", 1L, LocalDateTime.now());
        itemShortDto = new ItemShortDto(1L, "Drill", 1L);
        itemRequestDto = new ItemRequestDto(1L, "Need a drill", userDto, LocalDateTime.now(), List.of(itemShortDto));
        item = new Item(1L, "Drill", "Drill tool", true, user, itemRequest);
        itemDto = new ItemDto(1L, "Drill", "Drill tool", true, userDto, null, null, null, List.of());

        Long userId = 1L;
        currentUser = new User();
        currentUser.setId(userId);
        currentUser.setName("John Doe");

        anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("Jane Smith");

        // Первый запрос другого пользователя
        firstRequest = new ItemRequest();
        firstRequest.setId(1L);
        firstRequest.setDescription("First Request");
        firstRequest.setRequestor(anotherUser);

        // Второй запрос текущего пользователя
        secondRequest = new ItemRequest();
        secondRequest.setId(2L);
        secondRequest.setDescription("Second Request");
        secondRequest.setRequestor(currentUser);

        // Товары, относящиеся к первому запросу
        firstItem = new Item();
        firstItem.setId(1L);
        firstItem.setName("First Item");
        firstItem.setRequest(firstRequest);
        firstItem.setOwner(currentUser);

        // Вещь, связанная со вторым запросом (текущего пользователя)
        secondItem = new Item();
        secondItem.setId(2L);
        secondItem.setName("Second Item");
        secondItem.setRequest(secondRequest);
        secondItem.setOwner(anotherUser);

    }


    @Test
    public void testAddItemRequest_Successfully() {

        Long userId = 1L;
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(itemRequestRepository.save(any(ItemRequest.class)))
                .willAnswer(i -> i.getArgument(0));

        ItemRequestDto result = itemRequestService.addItemRequest(itemRequestCreateDto, userId);

        assertThat(result.getDescription()).isEqualTo("Need a drill");
        assertThat(result.getRequestor().getId()).isEqualTo(userId);
    }

    @Test
    public void testAddItemRequest_UserNotFound() {

        Long userId = 999L;
        given(userRepository.findById(userId))
                .willReturn(java.util.Optional.empty());

        Throwable exception = assertThrows(RuntimeException.class, () ->
                itemRequestService.addItemRequest(itemRequestCreateDto, userId));
        assertThat(exception.getMessage())
                .isEqualTo("User with id: 999 not found.");
    }

    @Test
    public void testGetListOfYourRequests_Successful() {

        Long userId = 1L;
        List<ItemRequest> requests = List.of(itemRequest);
        List<Item> items = List.of(item);

        given(userRepository.findById(userId))
                .willReturn(java.util.Optional.of(user));
        given(itemRequestRepository.getItemRequestByRequestor_IdOrderByCreatedAsc(userId))
                .willReturn(requests);
        given(itemRepository.findAllByRequest_IdNotNull())
                .willReturn(items);

        Collection<ItemRequestDto> result = itemRequestService.getListOfYourRequests(userId);

        assertThat(result).isNotEmpty();
        assertThat(result.iterator().next().getItems()).isNotEmpty();
    }

    @Test
    public void testGetListOfYourRequests_NoRequests() {
        // given
        Long userId = 1L;
        List<ItemRequest> requests = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        given(userRepository.findById(userId))
                .willReturn(java.util.Optional.of(user));
        given(itemRequestRepository.getItemRequestByRequestor_IdOrderByCreatedAsc(userId))
                .willReturn(requests);
        given(itemRepository.findAllByRequest_IdNotNull())
                .willReturn(items);

        Collection<ItemRequestDto> result = itemRequestService.getListOfYourRequests(userId);

        assertThat(result).isEmpty();
    }

    @Test
    public void testGetListOfYourRequests_UserNotFound() {

        Long unknownUserId = 999L;

        given(userRepository.findById(unknownUserId))
                .willReturn(java.util.Optional.empty());


        Throwable exception = assertThrows(RuntimeException.class, () ->
                itemRequestService.getListOfYourRequests(unknownUserId));
        assertThat(exception.getMessage()).startsWith("User with id: 999 not found.");
    }

    @Test
    public void testGetListQueriesCreatedByOtherUsers_Successful() {

        Long userId = 1L;

        List<ItemRequest> allRequests = List.of(firstRequest, secondRequest);
        List<Item> allItems = List.of(firstItem, secondItem);

        given(userRepository.findById(userId))
                .willReturn(java.util.Optional.of(currentUser));
        given(itemRequestRepository.findAll())
                .willReturn(allRequests);
        given(itemRepository.findAllByRequest_IdNotNull())
                .willReturn(allItems);

        Collection<ItemRequestDto> result = itemRequestService.getListQueriesCreatedByOtherUsers(userId);

        assertThat(result).isNotEmpty();
        assertThat(result.iterator().next().getItems()).isNotEmpty();
    }

    @Test
    public void testGetListQueriesCreatedByOtherUsers_UnauthorizedUser() {

        Long unknownUserId = 999L;

        given(userRepository.findById(unknownUserId))
                .willReturn(java.util.Optional.empty());

        Throwable exception = assertThrows(RuntimeException.class, () ->
                itemRequestService.getListQueriesCreatedByOtherUsers(unknownUserId));
        assertThat(exception.getMessage()).startsWith("User with id: 999 not found.");
    }

    @Test
    public void testGetItemRequestById_Successful() {
        // given
        Long itemRequestId = 1L;
        List<Item> items = List.of(item);

        given(itemRequestRepository.findById(itemRequestId))
                .willReturn(Optional.of(itemRequest));
        given(itemRepository.findAllByRequest_Id(itemRequestId))
                .willReturn(items);

        // when
        ItemRequestDto result = itemRequestService.getItemRequestById(itemRequestId);

        // then
        assertThat(result.getDescription()).isEqualTo("Need a drill");
        assertThat(result.getItems()).isNotEmpty();
        assertThat(result.getItems().iterator().next().getName()).isEqualTo("Drill");
    }

    @Test
    public void testGetItemRequestById_NotFound() {

        Long missingItemRequestId = 999L;

        given(itemRequestRepository.findById(missingItemRequestId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> itemRequestService.getItemRequestById(missingItemRequestId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("ItemRequest with id:");
    }

    @Test
    public void testGetItemRequestById_CorrectStructure() {
        // given
        Long itemRequestId = 1L;
        List<Item> items = List.of(item);

        given(itemRequestRepository.findById(itemRequestId))
                .willReturn(Optional.of(itemRequest));
        given(itemRepository.findAllByRequest_Id(itemRequestId))
                .willReturn(items);

        ItemRequestDto result = itemRequestService.getItemRequestById(itemRequestId);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Need a drill");
        assertThat(result.getItems()).size().isEqualTo(1);
        assertThat(result.getItems().iterator().next().getName()).isEqualTo("Drill");
    }

}