package com.practice.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareit.item.Item;
import com.practice.shareit.user.User;
import com.practice.shareit.utils.RequestConstants;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookingControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void updateName() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user1@gmail.com");

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

        String jsonPost = objectMapper.writeValueAsString(bookingCreateDto);

        String contentAsString = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost)
                        .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookingReadDto bookingReadDto = objectMapper.readValue(contentAsString, BookingReadDto.class);
        int id = bookingReadDto.getId();

        mockMvc.perform(patch("/bookings/" + id)
                        .header(RequestConstants.USER_ID_HEADER, user.getId())
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value(Status.REJECTED.toString()))
                .andExpect(jsonPath("$.item.id").value(item.getId()));
    }
}
