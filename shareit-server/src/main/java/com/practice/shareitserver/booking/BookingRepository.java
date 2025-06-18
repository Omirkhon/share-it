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
    Page<Booking> findAllByBookerOrderByStartDateDesc(User booker, Pageable pageable);

    Page<Booking> findAllByItemOwnerOrderByStartDateDesc(User itemOwner, Pageable pageable);

    List<Booking> findByBookerAndItem(User booker, Item item);

    Page<Booking> findByBookerAndStatusOrderByStartDateDesc(User booker,
                                                            BookingStatus status,
                                                            Pageable pageable);

    // Future
    Page<Booking> findByBookerAndStartDateIsAfterOrderByStartDateDesc(User booker,
                                                                      LocalDateTime startDateAfter,
                                                                      Pageable pageable);

    // Current
    Page<Booking> findByBookerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(User booker,
                                                                                        LocalDateTime startDateBefore,
                                                                                        LocalDateTime endDateAfter,
                                                                                        Pageable pageable);

    // Past
    Page<Booking> findByBookerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(User booker,
                                                                              BookingStatus status,
                                                                              LocalDateTime endDateBefore,
                                                                              Pageable pageable);

    Page<Booking> findByItemOwnerAndStatusOrderByStartDateDesc(User owner,
                                                               BookingStatus status,
                                                               Pageable pageable);

    // Future
    Page<Booking> findByItemOwnerAndStartDateIsAfterOrderByStartDateDesc(User owner,
                                                                         LocalDateTime startDateAfter,
                                                                         Pageable pageable);

    // Current
    Page<Booking> findByItemOwnerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(User owner,
                                                                                           LocalDateTime startDateBefore,
                                                                                           LocalDateTime endDateAfter,
                                                                                           Pageable pageable);

    // Past
    Page<Booking> findByItemOwnerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(User owner,
                                                                                 BookingStatus status,
                                                                                 LocalDateTime endDateBefore,
                                                                                 Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.startDate < ?2 " +
            "and b.status = com.practice.shareitserver.booking.BookingStatus.APPROVED " +
            "order by b.endDate desc " +
            "limit 1")
    Optional<Booking> findFirstByItemIdAndStartDateIsBeforeOrderByEndDateDesc(int itemId, LocalDateTime startDateBefore);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.startDate > ?2 " +
            "and b.status = com.practice.shareitserver.booking.BookingStatus.APPROVED " +
            "order by b.startDate " +
            "limit 1")
    Optional<Booking> findFirstByItemIdAndStartDateIsAfterOrderByStartDate(int itemId, LocalDateTime startDateAfter);
}
