package com.practice.shareitserver.booking;

import com.practice.shareitserver.item.Item;
import com.practice.shareitserver.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBooker(User booker, Pageable pageable);

    Page<Booking> findAllByItemOwner(User itemOwner, Pageable pageable);

    List<Booking> findByBookerAndItem(User booker, Item item);
}
