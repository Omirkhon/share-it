package com.practice.shareit.booking;

import com.practice.shareit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker(User booker);
}
