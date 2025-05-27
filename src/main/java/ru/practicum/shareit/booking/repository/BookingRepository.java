package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker(User booker, Sort sort);

    List<Booking> findByBooker_IdAndItem_IdOrderByStartDesc(Long userId, Long itemId);

    List<Booking> findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(Long userId,
                                                                          Long itemId,
                                                                          BookingStatus status,
                                                                          LocalDateTime end);

    List<Booking> findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(User booker,
                                                                                          LocalDateTime nowStart,
                                                                                          LocalDateTime nowStop);

    List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStartAfterOrderByStartAsc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStatusEqualsOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findByItem_Owner(User owner, Sort sort);

    List<Booking> findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(User owner,
                                                                                              LocalDateTime nowStart,
                                                                                              LocalDateTime nowStop);

    List<Booking> findByItem_OwnerAndStartAfterOrderByStartAsc(User owner, LocalDateTime now);

    List<Booking> findByItem_OwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findByItem_OwnerAndStatusEqualsOrderByStartDesc(User owner, BookingStatus status);

}
