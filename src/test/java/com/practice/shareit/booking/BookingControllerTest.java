package com.practice.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareit.comment.CommentMapper;
import com.practice.shareit.item.Item;
import com.practice.shareit.item.ItemMapper;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserMapper;
import com.practice.shareit.utils.RequestConstants;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class, BookingService.class, UserMapper.class, ItemMapper.class, BookingMapper.class, CommentMapper.class})
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockitoBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    @SneakyThrows
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
        booking.setId(1000);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setStart(booking.getStartDate());
        bookingCreateDto.setEnd(booking.getEndDate());
        bookingCreateDto.setItemId(item.getId());

        String json = objectMapper.writeValueAsString(bookingCreateDto);

        when(bookingService.create(Mockito.anyInt(), Mockito.any()))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @SneakyThrows
    void findById_epicSuccess() {
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
        booking.setId(1000);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2024, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2025, 2, 20, 2, 1, 1));
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingService.findById(Mockito.anyInt()))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/" + booking.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @SneakyThrows
    void findAllByCurrentUser_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");

        Item item2 = new Item();
        item2.setId(1);
        item2.setDescription("Вещь");
        item2.setAvailable(true);
        item2.setName("Вещь");

        Booking booking = new Booking();
        booking.setId(1000);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(1001);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 12, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 24, 2, 1, 1));
        booking2.setItem(item2);
        booking2.setBooker(user);

        when(bookingService.findAllByCurrentUser(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking, booking2));

        mockMvc.perform(get("/bookings")
                        .header(RequestConstants.USER_ID_HEADER, user.getId())
                        .param("state", "FUTURE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].status").exists())
                .andExpect(jsonPath("$[0].item.id").value(item.getId()))
                .andExpect(jsonPath("$[1].id").value(booking2.getId()))
                .andExpect(jsonPath("$[1].status").exists())
                .andExpect(jsonPath("$[1].item.id").value(item2.getId()));
    }

    @Test
    @SneakyThrows
    void findAllByOwner_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user200@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setDescription("Вещь");
        item.setAvailable(true);
        item.setName("Вещь");

        Item item2 = new Item();
        item2.setId(1);
        item2.setDescription("Вещь");
        item2.setAvailable(true);
        item2.setName("Вещь");

        Booking booking = new Booking();
        booking.setId(1000);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        Booking booking2 = new Booking();
        booking2.setId(1001);
        booking2.setStatus(Status.APPROVED);
        booking2.setStartDate(LocalDateTime.of(2025, 12, 20, 2, 1, 1));
        booking2.setEndDate(LocalDateTime.of(2026, 2, 24, 2, 1, 1));
        booking2.setItem(item2);
        booking2.setBooker(user);

        when(bookingService.findAllByOwner(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking, booking2));

        mockMvc.perform(get("/bookings/owner")
                        .header(RequestConstants.USER_ID_HEADER, user.getId())
                        .param("state", "FUTURE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].status").exists())
                .andExpect(jsonPath("$[0].item.id").value(item.getId()))
                .andExpect(jsonPath("$[1].id").value(booking2.getId()))
                .andExpect(jsonPath("$[1].status").exists())
                .andExpect(jsonPath("$[1].item.id").value(item2.getId()));
    }

    @Test
    @SneakyThrows
    void updateStatus_epicSuccess() {
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
        booking.setId(1000);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2025, 10, 20, 2, 1, 1));
        booking.setEndDate(LocalDateTime.of(2026, 2, 20, 2, 1, 1));
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingService.updateStatus(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean()))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/" + booking.getId())
                        .header(RequestConstants.USER_ID_HEADER, user.getId())
                        .param("approved", "false"))
                .andExpect(status().isOk());
    }
}
