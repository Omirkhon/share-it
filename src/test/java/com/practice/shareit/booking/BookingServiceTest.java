package com.practice.shareit.booking;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.exceptions.ValidationException;
import com.practice.shareit.item.Item;
import com.practice.shareit.item.ItemRepository;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    BookingService bookingService;

    @Test
    void create_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");

        Booking booking = new Booking();
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(booking.getStartDate());
        bookingCreateDto.setEnd(booking.getEndDate());
        bookingCreateDto.setItemId(item.getId());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        Booking savedBooking = bookingService.create(user.getId(), bookingCreateDto);

        assertEquals(Status.APPROVED.toString(), savedBooking.getStatus().toString());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booking.getStartDate().toString(), savedBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), savedBooking.getEndDate().toString());
    }

    @Test
    void create_epicFail_UserNotFound() {
        String message = "Пользователь не найден";

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        bookingCreateDto.setEnd(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        bookingCreateDto.setItemId(item.getId());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.create(1, bookingCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_epicFail_incorrectTime() {
        String message = "Некорректно указано время";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        bookingCreateDto.setEnd(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        bookingCreateDto.setItemId(item.getId());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.create(1, bookingCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_epicFail_itemNotFound() {
        String message = "Вещь не найдена";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        bookingCreateDto.setEnd(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        bookingCreateDto.setItemId(1);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.create(1, bookingCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_epicFail_itemNotAvailable() {
        String message = "Вещь не доступна";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(false);
        item.setName("Вещь");

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        bookingCreateDto.setEnd(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        bookingCreateDto.setItemId(item.getId());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.create(1, bookingCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findById_epicSuccess() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.findById(booking.getId());

        assertEquals(booking.getStatus().toString(), foundBooking.getStatus().toString());
        assertEquals(booking.getStartDate().toString(), foundBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), foundBooking.getEndDate().toString());
    }

    @Test
    void findById_epicFail_notFound() {
        String message = "Бронь не найдена.";

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findById(1));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAllByCurrentUser_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByBooker(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), null, 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_withState_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByBooker(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "CURRENT", 0, 5);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_epicFail_UserNotFound() {
        String message = "Пользователь не найден";

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findAllByCurrentUser(1, null, 0, 5));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAllByOwner_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2023, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByItemOwner(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "PAST", 0, 5);

        assertEquals(List.of(booking), bookings);
    }

    @Test
    void findAllByOwner_epicFail_userNotFound() {
        String message = "Пользователь не найден.";

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(1, null, 0, 5));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAllByOwner_epicFail_noBookings() {
        String message = "У данного пользователя нет бронирований.";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByItemOwner(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of()));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(1, null, 0, 5));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void updateStatusToRejected_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        Booking updatedBooking = bookingService.updateStatus(user.getId(), booking.getId(), false);

        assertEquals(Status.REJECTED.toString(), updatedBooking.getStatus().toString());
        assertEquals(booking.getId(), updatedBooking.getId());
        assertEquals(booking.getStartDate().toString(), updatedBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), updatedBooking.getEndDate().toString());
        assertEquals(booking.getItem().getId(), updatedBooking.getItem().getId());
        assertEquals(booking.getBooker().getId(), updatedBooking.getBooker().getId());
    }

    @Test
    void updateStatusToApproved_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        Booking updatedBooking = bookingService.updateStatus(user.getId(), booking.getId(), true);

        assertEquals(Status.APPROVED.toString(), updatedBooking.getStatus().toString());
        assertEquals(booking.getId(), updatedBooking.getId());
        assertEquals(booking.getStartDate().toString(), updatedBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), updatedBooking.getEndDate().toString());
        assertEquals(booking.getItem().getId(), updatedBooking.getItem().getId());
        assertEquals(booking.getBooker().getId(), updatedBooking.getBooker().getId());
    }

    @Test
    void updateStatus_epicFail_bookingNotFound() {
        String message = "Бронь не найдена.";

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.updateStatus(1, 1, true));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void updateStatus_epicFail_CannotApproveBooking() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        String message = "Вы не можете подтверждать данную бронь";

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.updateStatus(2, booking.getId(), true));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void updateStatus_epicFail_userNotFound() {
        String message = "Пользователь не найден";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.updateStatus(1, 1, true));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void filterBookings_caseFuture() {
        String state = "FUTURE";

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2024, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        List<Booking> bookings = bookingService.filterBookings(List.of(booking, booking2), state);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void filterBookings_caseWaiting() {
        String state = "WAITING";

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2024, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        List<Booking> bookings = bookingService.filterBookings(List.of(booking, booking2), state);

        assertEquals(List.of(booking), bookings);
    }

    @Test
    void filterBookings_caseRejected() {
        String state = "REJECTED";

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2024, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.REJECTED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        List<Booking> bookings = bookingService.filterBookings(List.of(booking, booking2), state);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void filterBookings_caseCurrent_timeIsWrong() {
        String state = "CURRENT";

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2022, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2023, 2, 20, 2, 1, 1));

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.REJECTED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        List<Booking> bookings = bookingService.filterBookings(List.of(booking, booking2), state);

        assertEquals(List.of(), bookings);
    }

    @Test
    void filterBookings_wrongState() {
        String state = "A";

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(Status.WAITING);
        booking.setStartDate(LocalDateTime.of(2022, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2023, 2, 20, 2, 1, 1));

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(Status.REJECTED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));

        List<Booking> bookings = bookingService.filterBookings(List.of(booking, booking2), state);

        assertEquals(List.of(booking, booking2), bookings);
    }
}
