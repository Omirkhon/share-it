package com.practice.shareit.service;

import com.practice.shareit.item.Item;
import com.practice.shareit.item.ItemCreateDto;
import com.practice.shareit.item.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class ItemServiceIT {
    @Autowired
    ItemService itemService;

    @Test
    void findAllOwnItems_success() {
        int userId = 1;
        int expectedItemId = 1;

        List<Item> items = itemService.findAllOwnItems(userId, 0, 5);
        Item item = items.get(0);

        assertEquals(expectedItemId, item.getId());
    }

    @Test
    void findAllOwnItems_wrongUserId() {
        int userId = 10000;
        String message = "Пользователь не найден";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> itemService.findAllOwnItems(userId, 0, 5));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_wrongUserId() {
        int userId = 10000;
        String message = "Пользователь не найден";
        ItemCreateDto itemCreateDto = new ItemCreateDto();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> itemService.create(userId, itemCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_wrongRequestId() {
        int userId = 1;
        int requestId = 10000;
        String message = "Запрос не найден";
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setRequestId(requestId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> itemService.create(userId, itemCreateDto));

        assertEquals(message, exception.getMessage());
    }
}