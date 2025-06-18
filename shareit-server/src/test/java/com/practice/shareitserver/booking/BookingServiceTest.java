package com.practice.shareitserver.booking;

import com.practice.shareitserver.exceptions.NotFoundException;
import com.practice.shareitserver.exceptions.ValidationException;
import com.practice.shareitserver.item.Item;
import com.practice.shareitserver.item.ItemRepository;
import com.practice.shareitserver.user.User;
import com.practice.shareitserver.user.UserRepository;
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

        User owner = new User();
        owner.setId(2);
        owner.setName("Пользователь");
        owner.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
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

        assertEquals(BookingStatus.APPROVED.toString(), savedBooking.getStatus().toString());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booking.getStartDate().toString(), savedBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), savedBooking.getEndDate().toString());
    }

    @Test
    void create_epicFail_bookingOwnItem() {
        String message = "Вы не можете запрашивать собственную вещь";

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
        booking.setStatus(BookingStatus.APPROVED);
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

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.create(user.getId(), bookingCreateDto));

        assertEquals(message, exception.getMessage());
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
        User user = new User();
        user.setId(1);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.findById(user.getId(), booking.getId());

        assertEquals(booking.getStatus().toString(), foundBooking.getStatus().toString());
        assertEquals(booking.getStartDate().toString(), foundBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), foundBooking.getEndDate().toString());
    }

    @Test
    void findById_epicSuccess2() {
        User user = new User();
        user.setId(1);

        User user2 = new User();
        user2.setId(2);

        Item item = new Item();
        item.setId(1);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user2);
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.findById(user.getId(), booking.getId());

        assertEquals(booking.getStatus().toString(), foundBooking.getStatus().toString());
        assertEquals(booking.getStartDate().toString(), foundBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), foundBooking.getEndDate().toString());
    }

    @Test
    void findById_epicFail_notFound() {
        String message = "Бронь не найдена.";

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findById(1, 1));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findById_epicFail_bookingNotFound() {
        String message = "У вас такой брони не найдено";

        User user = new User();
        user.setId(1);

        Item item = new Item();
        item.setId(1);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findById(2, 1));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStateALL() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.REJECTED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByBookerOrderByStartDateDesc(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "ALL", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStateWAITING() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.WAITING);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndStatusOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "WAITING", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStateREJECTED() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.REJECTED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.WAITING);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndStatusOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "REJECTED", 0, 5);

        assertEquals(List.of(booking), bookings);
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStatePAST() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2023, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2024, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2022, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2022, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "PAST", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStateCURRENT() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "CURRENT", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByCurrentUser_epicSuccessWithStateFUTURE() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndStartDateIsAfterOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByCurrentUser(user.getId(), "FUTURE", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
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
    void findAllByOwner_epicSuccess_statePAST() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2023, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByItemOwnerAndStatusAndEndDateIsBeforeOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "PAST", 0, 5);

        assertEquals(List.of(booking), bookings);
    }

    @Test
    void findAllByOwner_epicSuccess_stateCURRENT() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2023, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2020, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByItemOwnerAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking2)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "CURRENT", 0, 5);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void findAllByOwner_epicSuccess_stateFUTURE() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByItemOwnerAndStartDateIsAfterOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "FUTURE", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByOwner_epicSuccess_stateWAITING() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.WAITING);
        booking2.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByItemOwnerAndStatusOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "WAITING", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByOwner_epicSuccess_stateREJECTED() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.REJECTED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.REJECTED);
        booking2.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByItemOwnerAndStatusOrderByStartDateDesc(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "REJECTED", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
    }

    @Test
    void findAllByOwner_epicSuccess_stateALL() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStatus(BookingStatus.REJECTED);
        booking.setStartDate(LocalDateTime.of(2022, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2024, 2, 20, 2, 1, 1));
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2026, 10, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2027, 2, 20, 2, 1, 1));
        booking2.setBooker(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findAllByItemOwnerOrderByStartDateDesc(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking, booking2)));

        List<Booking> bookings = bookingService.findAllByOwner(user.getId(), "ALL", 0, 5);

        assertEquals(List.of(booking, booking2), bookings);
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
        booking.setStatus(BookingStatus.APPROVED);
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

        assertEquals(BookingStatus.REJECTED.toString(), updatedBooking.getStatus().toString());
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
        booking.setStatus(BookingStatus.WAITING);
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

        assertEquals(BookingStatus.APPROVED.toString(), updatedBooking.getStatus().toString());
        assertEquals(booking.getId(), updatedBooking.getId());
        assertEquals(booking.getStartDate().toString(), updatedBooking.getStartDate().toString());
        assertEquals(booking.getEndDate().toString(), updatedBooking.getEndDate().toString());
        assertEquals(booking.getItem().getId(), updatedBooking.getItem().getId());
        assertEquals(booking.getBooker().getId(), updatedBooking.getBooker().getId());
    }

    @Test
    void updateStatus_epicFail_alreadyApproved() {
        String message = "Бронь уже подтверждена";

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
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.updateStatus(user.getId(), booking.getId(), true));

        assertEquals(message, exception.getMessage());
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
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        String message = "Вы не можете подтверждать данную бронь";

        when(bookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.updateStatus(2, booking.getId(), true));

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
        booking.setStatus(BookingStatus.WAITING);
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
}
