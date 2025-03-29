package com.practice.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    @SneakyThrows
    void create() {
        int userId = 2;
        int requestId = 1;
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setDescription("Полезная вещь");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(requestId);

        String json = objectMapper.writeValueAsString(itemCreateDto);

        mockMvc.perform(post("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("userId", Integer.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(itemCreateDto.getName()))
                .andExpect(jsonPath("$.description").value(itemCreateDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemCreateDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemCreateDto.getRequestId()));
    }
}
