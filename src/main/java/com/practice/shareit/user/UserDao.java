package com.practice.shareit.user;

import com.practice.shareit.exceptions.AuthorisationException;
import com.practice.shareit.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private int uniqueId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public UserDto create(UserDto userDto) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (userDto.getEmail().equalsIgnoreCase(entry.getValue().getEmail())) {
                throw new AuthorisationException("Пользователь с таким адрессом эл. почты уже существует.");
            }
        }
        if (userDto.getId() == 0) {
            userDto.setId(++uniqueId);
        }
        users.put(userDto.getId(), new User(userDto.getId(), userDto.getName(), userDto.getEmail()));
        return userDto;
    }

    public List<User> findAll() {
        return users.values().stream().toList();
    }

    public User findById(int id) {
        return users.get(id);
    }

    public User update(User user, int userId) {
        if (users.get(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!users.get(userId).getEmail().equalsIgnoreCase(user.getEmail())) {
            for (Map.Entry<Integer, User> entry : users.entrySet()) {
                if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(entry.getValue().getEmail())) {
                    throw new AuthorisationException("Пользователь с таким адрессом эл. почты уже существует.");
                }
            }
        }
        User oldUser = users.get(userId);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        users.put(userId, oldUser);
        return oldUser;
    }

    public void delete(int userId) {
        users.remove(userId);
    }
}
