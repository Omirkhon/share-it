package com.practice.shareitserver.item;

import com.practice.shareitserver.user.User;
import com.practice.shareitserver.user.UserDto;
import com.practice.shareitserver.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class ItemServiceIT {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;

    @Test
    void updateName() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("monkey@gmail.com");

        User user = userService.create(userDto);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("Описание");

        Item item = itemService.create(user.getId(), itemCreateDto);

        ItemDto newItem = new ItemDto();
        newItem.setName("Новое название");

        Item result = itemService.update(user.getId(), item.getId(), newItem);

        assertEquals(newItem.getName(), result.getName());
        assertEquals(itemCreateDto.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getAvailable(), result.getAvailable());
    }

    @Test
    void updateDescription() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("a@gmail.com");

        User user = userService.create(userDto);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("Описание");

        Item item = itemService.create(user.getId(), itemCreateDto);

        ItemDto newItem = new ItemDto();
        newItem.setDescription("Новое описание");

        Item result = itemService.update(user.getId(), item.getId(), newItem);

        assertEquals(newItem.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getName(), result.getName());
        assertEquals(itemCreateDto.getAvailable(), result.getAvailable());
    }

    @Test
    void updateAvailable() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("b@gmail.com");

        User user = userService.create(userDto);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("Описание");

        Item item = itemService.create(user.getId(), itemCreateDto);

        ItemDto newItem = new ItemDto();
        newItem.setAvailable(false);

        Item result = itemService.update(user.getId(), item.getId(), newItem);

        assertEquals(newItem.getAvailable(), result.getAvailable());
        assertEquals(itemCreateDto.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getName(), result.getName());
    }
}