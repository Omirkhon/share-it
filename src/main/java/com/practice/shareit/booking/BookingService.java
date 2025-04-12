package com.practice.shareit.booking;
import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.exceptions.ValidationException;
import com.practice.shareit.item.Item;
import com.practice.shareit.item.ItemRepository;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        booking.setStartDate(bookingCreateDto.getStart());
        booking.setEndDate(bookingCreateDto.getEnd());
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new ValidationException("Некорректно указано время");
        }
        Item item = itemRepository.findById(bookingCreateDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна");
        }
        booking.setItem(item);
        booking.setBooker(booker);
        return bookingRepository.save(booking);
    }

    public Booking updateStatus(int ownerId, int bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена."));
        if (ownerId != booking.getItem().getOwner().getId()) {
            throw new ValidationException("Вы не можете подтверждать данную бронь");
        }

        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

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

    public List<Booking> findAllByCurrentUser(int userId, String state, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> pageResult = bookingRepository.findAllByBooker(user, pageable);
        List<Booking> bookings = pageResult.getContent();

        if (state == null) {
            return bookings;
        }

        return filterBookings(bookings, state);
    }

    public List<Booking> findAllByOwner(int userId, String state, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> pageResult = bookingRepository.findAllByItemOwner(user, pageable);
        List<Booking> bookings = pageResult.getContent();
        if (bookings.isEmpty()) {
            throw new NotFoundException("У данного пользователя нет бронирований.");
        }
        return filterBookings(bookings, state);
    }

    public List<Booking> filterBookings(List<Booking> bookings, String state) {
        List<Booking> filteredBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "PAST" ->
                    bookings.stream()
                            .filter(booking -> booking.getEndDate().isBefore(now))
                            .forEach(filteredBookings::add);
            case "CURRENT" ->
                    bookings.stream()
                            .filter(booking -> booking.getStartDate().isBefore(now) && booking.getEndDate().isAfter(now))
                            .forEach(filteredBookings::add);
            case "FUTURE" ->
                    bookings.stream()
                            .filter(booking -> booking.getStartDate().isAfter(now))
                            .forEach(filteredBookings::add);
            case "WAITING" ->
                    bookings.stream()
                            .filter(booking -> booking.getStatus() == Status.WAITING)
                            .forEach(filteredBookings::add);
            case "REJECTED" ->
                    bookings.stream()
                            .filter(booking -> booking.getStatus() == Status.REJECTED)
                            .forEach(filteredBookings::add);
            default ->
            {
                return bookings;
            }
        }
        return filteredBookings;
    }
}
