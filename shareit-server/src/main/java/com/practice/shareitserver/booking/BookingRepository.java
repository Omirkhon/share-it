package com.practice.shareitserver.booking;

import com.practice.shareitserver.item.Item;
import com.practice.shareitserver.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBookerOrderByStartDate(User booker, Pageable pageable);

    Page<Booking> findAllByItemOwnerOrderByStartDate(User itemOwner, Pageable pageable);

    List<Booking> findByBookerAndItem(User booker, Item item);

    Page<Booking> findByBookerAndStatusOrderByStartDate(User booker,
                                                        BookingStatus status,
                                                        Pageable pageable);

    // Future
    Page<Booking> findByBookerAndStatusAndStartDateIsAfter(User booker,
                                                           BookingStatus status,
                                                           LocalDateTime startDateAfter,
                                                           Pageable pageable);

    // Current
    Page<Booking> findByBookerAndStatusAndStartDateIsBeforeAndEndDateIsAfter(User booker,
                                                                             BookingStatus status,
                                                                             LocalDateTime startDateBefore,
                                                                             LocalDateTime endDateAfter,
                                                                             Pageable pageable);

    // Past
    Page<Booking> findByBookerAndStatusAndEndDateIsBefore(User booker,
                                                          BookingStatus status,
                                                          LocalDateTime endDateBefore,
                                                          Pageable pageable);

    Page<Booking> findByItemOwnerAndStatusOrderByStartDate(User owner,
                                                        BookingStatus status,
                                                        Pageable pageable);

    // Future
    Page<Booking> findByItemOwnerAndStatusAndStartDateIsAfter(User owner,
                                                           BookingStatus status,
                                                           LocalDateTime startDateAfter,
                                                           Pageable pageable);

    // Current
    Page<Booking> findByItemOwnerAndStatusAndStartDateIsBeforeAndEndDateIsAfter(User owner,
                                                                             BookingStatus status,
                                                                             LocalDateTime startDateBefore,
                                                                             LocalDateTime endDateAfter,
                                                                             Pageable pageable);

    // Past
    Page<Booking> findByItemOwnerAndStatusAndEndDateIsBefore(User owner,
                                                          BookingStatus status,
                                                          LocalDateTime endDateBefore,
                                                          Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.startDate < ?2 " +
            "order by b.endDate desc " +
            "limit 1")
    Optional<Booking> findFirstByItemIdAndStartDateIsBeforeOrderByEndDateDesc(int itemId, LocalDateTime startDateBefore);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.startDate > ?2 " +
            "order by b.startDate " +
            "limit 1")
    Optional<Booking> findFirstByItemIdAndStartDateIsAfterOrderByStartDate(int itemId, LocalDateTime startDateAfter);
}
