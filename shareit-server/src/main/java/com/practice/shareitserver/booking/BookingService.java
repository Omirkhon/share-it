package com.practice.shareitserver.booking;

import com.practice.shareitserver.exceptions.NotFoundException;
import com.practice.shareitserver.exceptions.ValidationException;
import com.practice.shareitserver.item.Item;
import com.practice.shareitserver.item.ItemRepository;
import com.practice.shareitserver.user.User;
import com.practice.shareitserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(bookingCreateDto.getStart());
        booking.setEndDate(bookingCreateDto.getEnd());
        if (!booking.getStartDate().isBefore(booking.getEndDate())) {
            throw new ValidationException("Некорректно указано время");
        }
        Item item = itemRepository.findById(bookingCreateDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна");
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Вы не можете запрашивать собственную вещь");
        }
        booking.setItem(item);
        booking.setBooker(booker);
        return bookingRepository.save(booking);
    }

    public Booking updateStatus(int ownerId, int bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена."));
        if (ownerId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("Вы не можете подтверждать данную бронь");
        }

        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (approved) {
            if (booking.getStatus() == BookingStatus.APPROVED) {
                throw new ValidationException("Бронь уже подтверждена");
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public Booking findById(int userId, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена."));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("У вас такой брони не найдено");
        }
        return booking;
    }

    public List<Booking> findAllByCurrentUser(int userId, String stateStr, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);

        LocalDateTime now = LocalDateTime.now();

        BookingState state = BookingState.from(stateStr);
        switch (state) {
            case WAITING:
                return bookingRepository.findByBookerAndStatusOrderByStartDateDesc(user, BookingStatus.WAITING, pageable).getContent();
            case REJECTED:
                return bookingRepository.findByBookerAndStatusOrderByStartDateDesc(user, BookingStatus.REJECTED, pageable).getContent();
            case PAST:
                return bookingRepository.findByBookerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(user, BookingStatus.APPROVED, now, pageable).getContent();
            case CURRENT:
                return bookingRepository.findByBookerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(user, now, now, pageable).getContent();
            case FUTURE:
                return bookingRepository.findByBookerAndStartDateIsAfterOrderByStartDateDesc(user, now, pageable).getContent();
            default:
                return bookingRepository.findAllByBookerOrderByStartDateDesc(user, pageable).getContent();
        }
    }


    public List<Booking> findAllByOwner(int userId, String stateStr, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime now = LocalDateTime.now();

        BookingState state = BookingState.from(stateStr);
        switch (state) {
            case WAITING:
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDateDesc(user, BookingStatus.WAITING, pageable).getContent();
            case REJECTED:
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDateDesc(user, BookingStatus.REJECTED, pageable).getContent();
            case PAST:
                return bookingRepository.findByItemOwnerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(user, BookingStatus.APPROVED, now, pageable).getContent();
            case CURRENT:
                return bookingRepository.findByItemOwnerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(user, now, now, pageable).getContent();
            case FUTURE:
                return bookingRepository.findByItemOwnerAndStartDateIsAfterOrderByStartDateDesc(user, now, pageable).getContent();
            default:
                return bookingRepository.findAllByItemOwnerOrderByStartDateDesc(user, pageable).getContent();
        }
    }
}
