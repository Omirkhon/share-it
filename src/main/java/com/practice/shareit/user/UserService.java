package com.practice.shareit.user;

import com.practice.shareit.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserDao userDao;
    Map<Integer, User> users = new HashMap<>();

    public UserDto create(UserDto user) {
        if (user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Имя или Эл. Почта пустые.");
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Неправильный формат эл. почты");
        }
        return userDao.create(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findById(int id) {
        return userDao.findById(id);
    }

    public User update(User user, int userId) {
        return userDao.update(user, userId);
    }

    public void delete(int userId) {
        userDao.delete(userId);
    }
}
