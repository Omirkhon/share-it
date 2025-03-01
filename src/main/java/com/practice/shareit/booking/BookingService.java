package com.practice.shareit.booking;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.item.ItemRepository;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import com.practice.shareit.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Booking create(int userId, BookingCreateDto bookingCreateDto) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setId(bookingCreateDto.getId());
        booking.setStartDate(bookingCreateDto.getStartDate());
        booking.setEndDate(bookingCreateDto.getEndDate());
        booking.setItem(bookingCreateDto.getItem());
        booking.setBooker(booker);
        return bookingRepository.save(booking);
    }

    public Booking updateStatus(int bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена."));

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public Booking findById(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена."));
    }

    public List<Booking> findAllByCurrentUser(int userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Booking> bookings = bookingRepository.findAllByBooker(user);

        if (state == null) {
            return bookings;
        }

        List<Booking> filteredBookings = new ArrayList<>();
        if (state.equals("PAST")) {
            bookings.stream()
                    .filter(booking -> booking.getEndDate().isBefore(LocalDateTime.now()))
                    .forEach(filteredBookings::add);
        } else if (state.equals("CURRENT")) {
            bookings.stream()
                    .filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()) && booking.getEndDate().isAfter(LocalDateTime.now()))
                    .forEach(filteredBookings::add);
        } else if (state.equals("FUTURE")) {
            bookings.stream()
                    .filter(booking -> booking.getStartDate().isAfter(LocalDateTime.now()))
                    .forEach(filteredBookings::add);
        } else if (state.equals("WAITING")) {
            bookings.stream()
                    .filter(booking -> booking.getStatus() == Status.WAITING)
                    .forEach(filteredBookings::add);
        } else if (state.equals("REJECTED")) {
            bookings.stream()
                    .filter(booking -> booking.getStatus() == Status.REJECTED)
                    .forEach(filteredBookings::add);
        }

        return filteredBookings;
    }

    public List<Booking> findAllByCurrentUserItems(int userId, String state) {
        return null;
    }
}
