package com.practice.shareit.user;

import com.practice.shareit.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Spy
    UserMapper userMapper;

    @Test
    void create_epicSuccess() {
        User user = new User();
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User savedUser = userService.create(userMapper.toDto(user));

        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void findAll_epicSuccess() {
        User user = new User();
        user.setName("Макака");
        user.setEmail("macaque@gmail.com");

        User user2 = new User();
        user2.setName("Горилла");
        user2.setEmail("gorilla@gmail.com");

        when(userRepository.findAll())
                .thenReturn(List.of(user, user2));

        List<User> users = userService.findAll();

        assertEquals(user, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    void findById_epicSuccess() {
        User user = new User();
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        User foundUser = userService.findById(user.getId());

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void findById_epicFail_wrongID() {
        String message = "Пользователь не найден";
        int wrongId = 1000;

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(wrongId));

        assertEquals(message, exception.getMessage());
    }
}
