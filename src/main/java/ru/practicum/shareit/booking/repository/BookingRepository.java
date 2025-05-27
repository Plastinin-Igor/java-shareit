package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findByBooker(User booker, Sort sort);

    // ALL
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long userId);

    // CURRENT
    List<Booking> findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(User booker,
                                                                                          LocalDateTime nowStart,
                                                                                          LocalDateTime nowStop);

    // PAST
    List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime now);

    // FUTURE
    List<Booking> findByBookerAndStartAfterOrderByStartAsc(User booker, LocalDateTime now);

    // WAITING - REJECT
    List<Booking> findByBookerAndStatusEqualsOrderByStartDesc(User booker, BookingStatus status);


    List<Booking> findByItem_Owner(User owner, Sort sort);

    List<Booking> findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(User owner,
                                                                                              LocalDateTime nowStart,
                                                                                              LocalDateTime nowStop);

    List<Booking> findByItem_OwnerAndStartAfterOrderByStartAsc(User owner, LocalDateTime now);

    List<Booking> findByItem_OwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findByItem_OwnerAndStatusInOrderByStartDesc(User owner, Collection<BookingStatus> statuses);

    List<Booking> findByItem_OwnerAndStatusEqualsOrderByStartDesc(User owner, BookingStatus status);

}
