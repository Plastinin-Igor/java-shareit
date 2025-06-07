package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import org.junit.jupiter.api.Test;

import org.mockito.Mockito;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.data.domain.Sort;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private BookingMapper bookingMapper;

    private Item item;
    private User user;
    private User userBooker;
    private Booking booking;

    @BeforeEach
    void setup() {
        user = new User(1L, "Plastinin Igor", "plastinin@ya.ru");
        item = new Item(1L, "Drill", "Drill tool", true, user, null);
        userBooker = new User(2L, "Plastinin Igor", "plastinin@ya.ru");
        booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), item, userBooker, BookingStatus.CANCELED);
    }


    @Test
    void addBooking_shouldReturnBookingDto() {
        Long bookerId = userBooker.getId();
        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2), item.getId(), userBooker.getId(), BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(bookingCreateDto.getItemId()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(userRepository.findById(bookerId))
                .thenReturn(Optional.ofNullable(userBooker));

        Mockito.lenient()
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingService.addBooking(bookingCreateDto, bookerId);

        assertNotNull(bookingDto);
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

        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));

        Mockito.when(bookingRepository.save(any()))
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


        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }


    @Test
    void getBookingById_shouldReturnBookingDto() {
        Long bookingId = booking.getId();
        Long userId = user.getId();


        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.ofNullable(booking));

        BookingDto bookingDto = bookingService.getBookingById(bookingId, userId);

        assertNotNull(bookingDto);
    }

    @Test
    void getBookingById_throwsObjectNotFoundException() {
        Long bookingId = booking.getId();
        Long userId = user.getId();


        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void shouldGetAllBookingsForUser() {
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

}