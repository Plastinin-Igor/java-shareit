package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto addBooking(BookingCreateDto bookingCreateDto, Long userId) {
        User user = getUser(userId);
        Item item = getItem(bookingCreateDto.getItemId());
        if (!item.getAvailable()) {
            log.error("The item with id: {} is not available for booking", item.getId());
            throw new IllegalArgumentException("The item with id: " + item.getId() + " is not available for booking");
        }
        Booking booking = BookingMapper.toBookingFromCreateDto(bookingCreateDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateBooking(BookingUpdateDto bookingUpdateDto, Long bookingId, Long userId) {
        getUser(userId);
        Booking oldBooking = getBooking(bookingId);
        if (!userId.equals(oldBooking.getBooker().getId())) {
            log.error("Only the owner can make changes booking.");
            throw new IllegalArgumentException("Only the owner can make changes booking.");
        }
        Booking newBooking = BookingMapper.toBookingFromUpdateDto(oldBooking, bookingUpdateDto);
        return BookingMapper.toBookingDto(bookingRepository.save(newBooking));
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId, Long userId) {
        Booking booking = getBooking(bookingId);
        if (!userId.equals(booking.getBooker().getId())) {
            log.error("Only the owner can make delete booking.");
            throw new IllegalArgumentException("Only the owner can make delete booking.");
        }
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getBooking(bookingId);
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            log.error("Only the owner can make approve booking.");
            throw new IllegalArgumentException("Only the owner can make approve booking.");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId) {
        return BookingMapper.toBookingDto(getBooking(bookingId));
    }

    @Override
    public Collection<BookingDto> getBookingByUser(Long userId, BookingState state) {
        User user = getUser(userId);

        return switch (state) {
            case ALL -> bookingRepository.findByBooker(user, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case CURRENT -> {
                LocalDateTime now = LocalDateTime.now();
                yield bookingRepository.findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(user,
                                now, now).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            }
            case PAST -> bookingRepository.findByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case FUTURE -> bookingRepository.findByBookerAndStartAfterOrderByStartAsc(user, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case WAITING -> bookingRepository.findByBookerAndStatusEqualsOrderByStartDesc(user, BookingStatus.WAITING)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case REJECTED -> bookingRepository.findByBookerAndStatusEqualsOrderByStartDesc(user, BookingStatus.REJECTED)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            default -> {
                log.error("Invalid booking state: {}.", state);
                throw new IllegalArgumentException("Invalid booking state: " + state + ".");
            }
        };
    }

    @Override
    public Collection<BookingDto> getBookingItemByUser(Long userId, BookingState state) {
        User user = getUser(userId);

        return switch (state) {
            case ALL -> bookingRepository.findByItem_Owner(user, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case CURRENT -> {
                LocalDateTime now = LocalDateTime.now();
                yield bookingRepository.findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                                user, now, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            }
            case PAST -> bookingRepository.findByItem_OwnerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case FUTURE -> bookingRepository.findByItem_OwnerAndStartAfterOrderByStartAsc(user, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case WAITING ->
                    bookingRepository.findByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookingStatus.WAITING)
                            .stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            case REJECTED ->
                    bookingRepository.findByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookingStatus.REJECTED)
                            .stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            default -> {
                log.error("Invalid booking state: {} for owner with id: {}.", state, userId);
                throw new IllegalArgumentException("Invalid booking state: " + state + ".");
            }
        };
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found."));

    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id: " + itemId + " not found."));
    }

    private Booking getBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found."));
    }

}
