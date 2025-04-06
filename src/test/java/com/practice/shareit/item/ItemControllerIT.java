package com.practice.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserDto;
import com.practice.shareit.utils.RequestConstants;
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
public class ItemControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void updateName() {
        UserDto userDto = new UserDto();
        userDto.setName("Пользователь");
        userDto.setEmail("user@gmail.com");

        String jsonUser = objectMapper.writeValueAsString(userDto);

        String contentAsStringUser = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .toString();

        UserDto user = objectMapper.readValue(contentAsStringUser, UserDto.class);
        int userId = user.getId();

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Товар");
        itemCreateDto.setDescription("Описание");
        itemCreateDto.setAvailable(true);

        String jsonPost = objectMapper.writeValueAsString(itemCreateDto);

        String contentAsString = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPost)
                .header(RequestConstants.HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .toString();

        Item item = objectMapper.readValue(contentAsString, Item.class);
        int id = item.getId();

        ItemDto newItem = new ItemDto();
        newItem.setName("Обновленный товар");

        String json = objectMapper.writeValueAsString(newItem);

        mockMvc.perform(patch("/items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newItem.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));
    }

    @Test
    @SneakyThrows
    void updateDescription() {

    }

    @Test
    @SneakyThrows
    void updateAvailable() {

    }
}
