package com.practice.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.shareit.comment.Comment;
import com.practice.shareit.comment.CommentMapper;
import com.practice.shareit.user.User;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest({ItemController.class, ItemMapper.class, CommentMapper.class, ItemService.class, ItemRepository.class})
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockitoBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void create_epicSuccess() {
        User user = new User();
        user.setId(1000);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Item item = new Item();
        item.setId(1000);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        String json = objectMapper.writeValueAsString(item);

        when(itemService.create(Mockito.anyInt(), Mockito.any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(RequestConstants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000))
                .andExpect(jsonPath("$.name").value("Предмет"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.comments").isEmpty());
    }

    @Test
    @SneakyThrows
    void findById_epicSuccess() {
        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        when(itemService.findById(Mockito.anyInt()))
                .thenReturn(item);

        mockMvc.perform(get("/items/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Предмет"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.comments").isEmpty());
    }

    @Test
    @SneakyThrows
    void update_epicSuccess() {
        User user = new User();
        user.setId(1000);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Item item = new Item();
        item.setId(1000);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        String json = objectMapper.writeValueAsString(item);

        when(itemService.update(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(item);

        mockMvc.perform(patch("/items/" + item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(RequestConstants.HEADER, user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createComment_epicSuccess() {
        User user = new User();
        user.setId(10);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Item item = new Item();
        item.setId(1000);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Комментарий");
        comment.setAuthor(user);

        String json = objectMapper.writeValueAsString(comment);

        when(itemService.createComment(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/" + item.getId() + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(RequestConstants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Комментарий"));
    }

    @Test
    @SneakyThrows
    void findAllOwnItems() {
        User user = new User();
        user.setId(10);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Предмет2");
        item2.setAvailable(false);
        item2.setDescription("Описание2");
        item2.setOwner(user);

        when(itemService.findAllOwnItems(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item, item2));

        mockMvc.perform(get("/items")
                        .header(RequestConstants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Предмет"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[0].comments").isEmpty())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Предмет2"))
                .andExpect(jsonPath("$[1].available").value(false))
                .andExpect(jsonPath("$[1].description").value("Описание2"))
                .andExpect(jsonPath("$[1].comments").isEmpty());
    }

    @Test
    @SneakyThrows
    void findByText() {
        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Название");
        item2.setAvailable(true);
        item2.setDescription("Описание предмета 2");

        when(itemService.findByText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item, item2));

        mockMvc.perform(get("/items/search")
                        .param("text", "пр"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Предмет"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[0].comments").isEmpty())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Название"))
                .andExpect(jsonPath("$[1].available").value(true))
                .andExpect(jsonPath("$[1].description").value("Описание предмета 2"))
                .andExpect(jsonPath("$[1].comments").isEmpty());
    }
}
