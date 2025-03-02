package com.practice.shareit.booking;

import com.practice.shareit.item.Item;
import com.practice.shareit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByItemOwner(User itemOwner);

    List<Booking> findByBookerAndItem(User booker, Item item);
}
