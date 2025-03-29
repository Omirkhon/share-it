package com.practice.shareit.user;

import com.practice.shareit.exceptions.AuthorisationException;
import com.practice.shareit.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        AuthorisationException exception = assertThrows(AuthorisationException.class, () -> userService.create(user2));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void updateName_epicSuccess() {
        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("Baboon@gmail.com");

        User user = userService.create(userDto);

        UserDto updatedUser = new UserDto();
        userDto.setName("Горилла");

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
        userDto.setEmail("monkey@gmail.com");

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
        String message = "Пользователь с такой эл.почтой уже существует";

        UserDto userDto = new UserDto();
        userDto.setName("Бабуин");
        userDto.setEmail("Baboon@gmail.com");

        User user = userService.create(userDto);

        UserDto updatedUser = new UserDto();
        userDto.setEmail("Nicola_Champlin4@yahoo.com");

        AuthorisationException exception = assertThrows(AuthorisationException.class, () -> userService.update(updatedUser, user.getId()));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void delete() {

    }
}
