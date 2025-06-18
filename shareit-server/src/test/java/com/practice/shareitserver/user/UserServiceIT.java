package com.practice.shareitserver.user;

import com.practice.shareitserver.exceptions.ConflictException;
import com.practice.shareitserver.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceIT {
    @Autowired
    UserService userService;

    @Test
    void create_epicFail_EmailExists() {
        String message = "Пользователь с такой эл. почтой уже существует";

        UserDto user = new UserDto();
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        UserDto user2 = new UserDto();
        user2.setName("Горилла");
        user2.setEmail("orangutan@gmail.com");

        userService.create(user);

        ConflictException exception = assertThrows(ConflictException.class, () -> userService.create(user2));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void updateName_epicSuccess() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("Baboon@gmail.com");

        User user = userService.create(userDto);

        UserDto updatedUser = new UserDto();
        updatedUser.setName("Горилла");

        User result = userService.update(updatedUser, user.getId());

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateEmail_epicSuccess() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("Baboon@gmail.com");

        User user = userService.create(userDto);

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("monkey@gmail.com");

        User result = userService.update(updatedUser, user.getId());

        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void update_epicFail_wrongID() {
        int id = 1000;
        String message = "Пользователь не найден";
        UserDto updatedUser = new UserDto();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.update(updatedUser, id));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void update_epicFail_emailExists() {
        String message = "Пользователь с такой эл. почтой уже существует";

        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("Baboon@gmail.com");

        User user = userService.create(userDto);

        UserDto userDto2 = new UserDto();
        userDto2.setName("Горилла");
        userDto2.setEmail("Gorilla@gmail.com");

        userService.create(userDto2);

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("Gorilla@gmail.com");

        ConflictException exception = assertThrows(ConflictException.class, () -> userService.update(updatedUser, user.getId()));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void deleteTest() {
        String message = "Пользователь не найден";

        UserDto userDto = new UserDto();
        userDto.setName("Vasily");
        userDto.setEmail("vasya123@gmail.com");

        User user = userService.create(userDto);
        int id = user.getId();
        userService.delete(id);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(id));

        assertEquals(message, exception.getMessage());
    }
}
