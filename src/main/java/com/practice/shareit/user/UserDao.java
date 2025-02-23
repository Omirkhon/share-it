package com.practice.shareit.user;

import com.practice.shareit.exception.AuthorisationException;
import com.practice.shareit.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDao {
    private int uniqueId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public User create(User user) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (user.getEmail().equalsIgnoreCase(entry.getValue().getEmail())) {
                throw new AuthorisationException("Пользователь с таким адрессом эл. почты уже существует.");
            }
        }
        if (user.getId() == 0) {
            user.setId(++uniqueId);
        }
        users.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return users.values().stream().toList();
    }

    public Optional<User> findById(int id) {
        return Optional.of(users.get(id));
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
