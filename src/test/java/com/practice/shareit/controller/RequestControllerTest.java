package com.practice.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareit.request.Request;
import com.practice.shareit.request.RequestController;
import com.practice.shareit.request.RequestMapper;
import com.practice.shareit.request.RequestRepository;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RequestController.class, RequestMapper.class})
@AutoConfigureMockMvc
public class RequestControllerTest {
    @MockitoBean
    RequestRepository requestRepository;
    @MockitoBean
    MockMvc mockMvc;
    @MockitoBean
    ObjectMapper objectMapper;
    @MockitoBean
    UserRepository userRepository;

    @Test
    @SneakyThrows
    public void findById_test() {

    }

    @Test
    @SneakyThrows
    public void create_test() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@gmail.com");

        Request request = new Request();
        request.setDescription("A");

        String json = objectMapper.writeValueAsString(request);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.save(Mockito.any()))
                .thenReturn(request);

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .param("userId", Integer.toString(user.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").value(request.getCreated()))
                .andExpect(jsonPath("$.items").isEmpty());
    }
}
