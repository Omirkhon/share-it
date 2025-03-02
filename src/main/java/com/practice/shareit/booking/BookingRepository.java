package com.practice.shareit.booking;

import com.practice.shareit.item.Item;
import com.practice.shareit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByItemOwner(User itemOwner);

    Optional<Booking> findBookingByBookerAndItem(User booker, Item item);
}
