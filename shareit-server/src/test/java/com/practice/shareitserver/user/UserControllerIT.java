package com.practice.shareitserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void updateName() {
        UserDto userDto = new UserDto();
        userDto.setName("Ааа");
        userDto.setEmail("Baboon@gmail.com");

        String jsonPost = objectMapper.writeValueAsString(userDto);

        String contentAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto user = objectMapper.readValue(contentAsString, UserDto.class);
        int id = user.getId();

        UserDto updatedUser = new UserDto();
        updatedUser.setName("Горилла");

        String json = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Горилла"))
                .andExpect(jsonPath("$.email").value("Baboon@gmail.com"));
    }

    @Test
    @SneakyThrows
    void updateEmail() {
        UserDto userDto = new UserDto();
        userDto.setName("Aaa");
        userDto.setEmail("Baboon@gmail.com");

        String jsonPost = objectMapper.writeValueAsString(userDto);

        String contentAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto user = objectMapper.readValue(contentAsString, UserDto.class);
        int id = user.getId();

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("aaa@gmail.com");

        String json = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Aaa"))
                .andExpect(jsonPath("$.email").value("aaa@gmail.com"));
    }
}
