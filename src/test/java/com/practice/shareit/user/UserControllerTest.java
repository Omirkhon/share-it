package com.practice.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest({UserController.class, UserMapper.class, UserService.class, UserRepository.class})
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockitoBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void create_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Обезьяна");
        user.setEmail("monke@gmail.com");

        String json = objectMapper.writeValueAsString(user);

        when(userService.create(Mockito.any()))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Обезьяна"))
                .andExpect(jsonPath("$.email").value("monke@gmail.com"));
    }

    @Test
    @SneakyThrows
    public void findAll_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Макака");
        user.setEmail("macaque@gmail.com");

        User user2 = new User();
        user2.setId(2);
        user2.setName("Горилла");
        user2.setEmail("gorilla@gmail.com");

        when(userService.findAll())
                .thenReturn(List.of(user, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Макака"))
                .andExpect(jsonPath("$[0].email").value("macaque@gmail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Горилла"))
                .andExpect(jsonPath("$[1].email").value("gorilla@gmail.com"));
    }

    @Test
    @SneakyThrows
    public void findById_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Шымпанзе");
        user.setEmail("chimp@gmail.com");

        when(userService.findById(Mockito.anyInt()))
                .thenReturn(user);

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Шымпанзе"))
                .andExpect(jsonPath("$.email").value("chimp@gmail.com"));
    }

    @Test
    @SneakyThrows
    void delete_epicSuccess() {
        User user = new User();
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void update() {
        User user = new User();
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        String json = objectMapper.writeValueAsString(user);

        when(userService.update(Mockito.any(), Mockito.anyInt()))
                .thenReturn(user);

        mockMvc.perform(patch("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
