package com.practice.shareitserver.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareitserver.comment.CommentMapper;
import com.practice.shareitserver.item.ItemMapper;
import com.practice.shareitserver.user.User;
import com.practice.shareitserver.utils.RequestConstants;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest({RequestController.class, CommentMapper.class, ItemMapper.class, RequestMapper.class})
@AutoConfigureMockMvc
public class RequestControllerTest {
    @MockitoBean
    RequestService requestService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    @SneakyThrows
    void create_EpicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Описание");

        String json = objectMapper.writeValueAsString(request);

        when(requestService.create(Mockito.anyInt(), Mockito.any()))
                .thenReturn(request);

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    @SneakyThrows
    void findById_EpicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Описание");
        request.setRequester(user);

        when(requestService.findById(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(request);

        mockMvc.perform(get("/requests/" + request.getId())
                        .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    @SneakyThrows
    void findAllByUser_EpicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Описание");
        request.setRequester(user);

        Request request2 = new Request();
        request2.setId(2);
        request2.setCreated(LocalDateTime.now());
        request2.setDescription("Описание запроса");
        request2.setRequester(user);

        when(requestService.findAllByUser(Mockito.anyInt()))
                .thenReturn(List.of(request, request2));

        mockMvc.perform(get("/requests")
                        .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].created").exists())
                .andExpect(jsonPath("$[1].description").value("Описание запроса"));
    }

    @Test
    @SneakyThrows
    void findByPageAndSize_EpicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Описание");
        request.setRequester(user);

        Request request2 = new Request();
        request2.setId(2);
        request2.setCreated(LocalDateTime.now());
        request2.setDescription("Описание запроса");
        request2.setRequester(user);

        when(requestService.findByPageAndSize(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(request, request2));

        mockMvc.perform(get("/requests/all")
                        .header(RequestConstants.USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].created").exists())
                .andExpect(jsonPath("$[1].description").value("Описание запроса"));
    }
}
