package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.service.UserService;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private BookingMapper bookingMapper;

    private Item item;
    private ItemDto itemDto;
    private User user;
    private UserDto userDto;
    private User userBooker;
    private BookingUpdateDto bookingUpdateDto;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setup() {
        user = new User(1L, "Plastinin Igor", "plastinin@ya.ru");

        item = new Item(1L, "Drill", "Drill tool", true, user, null);
        userBooker = new User(2L, "Plastinin Igor", "plastinin@ya.ru");
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), item, userBooker, BookingStatus.APPROVED);
        bookingDto = new BookingDto(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), new ItemDto(), new UserDto(), BookingStatus.CANCELED);
        bookingUpdateDto = new BookingUpdateDto(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), 1L, 1L, BookingStatus.CANCELED);
        userDto = new UserDto(1L, "Plastinin Igor", "plastinin@ya.ru");
        itemDto = new ItemDto(1L, "Drill", "Drill tool", true, userDto, 1L, new BookingShortDto(), new BookingShortDto(), List.of());

    }


    @Test
    void addBooking_shouldReturnBookingDto() {
        Long bookerId = userBooker.getId();
        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2), item.getId(), userBooker.getId(), BookingStatus.WAITING);

        when(itemRepository.findById(bookingCreateDto.getItemId()))
                .thenReturn(Optional.ofNullable(item));

        when(userRepository.findById(bookerId))
                .thenReturn(Optional.ofNullable(userBooker));

        Mockito.lenient()
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingService.addBooking(bookingCreateDto, bookerId);

        assertNotNull(bookingDto);
    }

    @Test
    void addBooking_ShouldFailIfItemIsUnavailable() {

        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2), item.getId(), userBooker.getId(), BookingStatus.WAITING);


        item.setAvailable(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.addBooking(bookingCreateDto, user.getId());
        }, "Expected an error because the item is unavailable");
    }

    @Test
    void addBooking_throwsUnavailableItemBookingException() {

        item = new Item(1L, "Drill", "Drill", false, user, null); // Доступность установлена в false

        Long bookerId = userBooker.getId();

        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2),
                item.getId(),
                userBooker.getId(),
                BookingStatus.WAITING
        );

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(bookingCreateDto, bookerId));
    }


    @Test
    void approveBooking_shouldReturnBookingDto() {
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        Long bookingId = booking.getId();
        Long userId = user.getId();
        boolean isApproved = true;

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingService.approveBooking(bookingId, userId, isApproved);

        assertNotNull(bookingDto);
    }

    @Test
    void approveBooking_throwsObjectNotFoundException() {
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        Long bookingId = booking.getId();
        Long userId = user.getId();
        boolean isApproved = true;


        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }


    @Test
    void approveBooking_SuccessfulRejectionByOwner() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDto result = bookingService.approveBooking(booking.getId(), user.getId(), false);


        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void approveBooking_FailureNonOwnerAttempt() {

        User nonOwner = new User(2L, "Petrov Petr", "petrov@mail.ru");
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.approveBooking(booking.getId(), nonOwner.getId(), true);
        });
    }

    @Test
    void approveBooking_FailureAlreadyApprovedBooking() {

        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.approveBooking(booking.getId(), user.getId(), true);
        });
    }

    @Test
    void approveBooking_FailureAlreadyRejectedBooking() {

        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.approveBooking(booking.getId(), user.getId(), false);
        });
    }

    @Test
    void getBookingById_shouldReturnBookingDto() {
        Long bookingId = booking.getId();
        Long userId = user.getId();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));

        BookingDto bookingDto = bookingService.getBookingById(bookingId, userId);

        assertNotNull(bookingDto);
    }

    @Test
    void getBookingById_throwsObjectNotFoundException() {
        Long bookingId = booking.getId();
        Long userId = user.getId();


        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getBookingById_ShouldDenyAccess_ToUnrelatedPerson() {

        User unrelatedUser = new User(3L, "Сергей Смирнов", "serge_smirnov@mail.ru");
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.getBookingById(booking.getId(), unrelatedUser.getId());
        }, "Доступ запрещён постороннему лицу");
    }

    @Test
    void shouldGetBookingsForUserAll() {
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        Booking booking1 = new Booking(2L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        List<Booking> bookings = Arrays.asList(booking, booking1);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        given(bookingRepository.findByBooker(any(User.class), any(Sort.class))).willReturn(bookings);

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.ALL);

        verify(bookingRepository).findByBooker(userBooker, Sort.by(Sort.Direction.DESC, "start"));
        assertThat(result.size()).isEqualTo(bookings.size());
    }


    @Test
    void shouldGetBookingsForUserCurrent() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                eq(userBooker), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.CURRENT);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetBookingsForUserPast() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByBookerAndEndBeforeOrderByStartDesc(
                eq(userBooker), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.PAST);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetBookingsForUserFuture() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByBookerAndStartAfterOrderByStartAsc(
                eq(userBooker), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.FUTURE);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetBookingsForUserWaiting() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByBookerAndStatusEqualsOrderByStartDesc(
                eq(userBooker), eq(BookingStatus.WAITING)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.WAITING);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void shouldGetBookingsForUserRejected() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByBookerAndStatusEqualsOrderByStartDesc(
                eq(userBooker), eq(BookingStatus.REJECTED)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingByUser(userBooker.getId(), BookingState.REJECTED);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetBookingsForUserThrowException_whenStateIsInvalid() {
        Long userId = userBooker.getId();

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.getBookingByUser(userId, BookingState.UNKNOWN);
        });
    }

    @Test
    void updateBooking_shouldSuccessfullyUpdateBooking_whenUserIsOwner() {
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        Long bookingId = booking.getId();
        Long userId = userBooker.getId();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));


        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(userBooker));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingService.updateBooking(bookingUpdateDto, bookingId, userId);

        assertNotNull(bookingDto);
    }


    @Test
    void updateBooking_ShouldFail_WhenUnauthorizedUserAttemptsToUpdateBooking() {

        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        Long bookingId = booking.getId();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));


        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userBooker));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.updateBooking(bookingUpdateDto, booking.getId(), user.getId());
        }, "Expected an error because the user isn't authorized to modify the booking");
    }

    @Test
    void deleteBooking_shouldThrowException_whenUserIsNotOwner() {

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.deleteBooking(booking.getId(), 44L);
        });

        assertEquals("Only the owner can make delete booking.", exception.getMessage());
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void deleteBooking_shouldDeleteBooking_whenUserIsOwner() {

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(booking.getId(), booking.getBooker().getId());

        verify(bookingRepository).delete(booking);
    }


    @Test
    void getBookingItemByUser_shouldReturnAllBookings_whenStateIsAll() {

        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);


        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));


        when(bookingRepository.findByItem_Owner(userBooker, Sort.by(Sort.Direction.DESC, "start"))).thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(2L, BookingState.ALL);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void getBookingItemByUser_shouldReturnCurrentBookings_whenStateIsCurrent() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                eq(userBooker), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(userId, BookingState.CURRENT);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getBookingItemByUser_shouldReturnCurrentBookings_whenStateIsPast() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByItem_OwnerAndEndBeforeOrderByStartDesc(
                eq(userBooker), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(userId, BookingState.PAST);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getBookingItemByUser_shouldReturnCurrentBookings_whenStateIsFuture() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByItem_OwnerAndStartAfterOrderByStartAsc(
                eq(userBooker), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(userId, BookingState.FUTURE);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getBookingItemByUser_shouldReturnCurrentBookings_whenStateIsWitting() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByItem_OwnerAndStatusEqualsOrderByStartDesc(
                eq(userBooker), eq(BookingStatus.WAITING))).thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(userId, BookingState.WAITING);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void getBookingItemByUser_shouldReturnCurrentBookings_whenStateIsRejected() {
        Long userId = userBooker.getId();

        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                item, userBooker, BookingStatus.WAITING);

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        when(bookingRepository.findByItem_OwnerAndStatusEqualsOrderByStartDesc(
                eq(userBooker), eq(BookingStatus.REJECTED))).thenReturn(List.of(booking));

        Collection<BookingDto> result = bookingService.getBookingItemByUser(userId, BookingState.REJECTED);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void getBookingItemByUser_shouldThrowException_whenStateIsInvalid() {
        Long userId = userBooker.getId();

        given(userRepository.findById(userBooker.getId()))
                .willReturn(Optional.of(userBooker));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.getBookingItemByUser(userId, BookingState.UNKNOWN);
        });
    }


}