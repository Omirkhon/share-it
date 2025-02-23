package com.practice.shareit.user;

import com.practice.shareit.exception.NotFoundException;
import com.practice.shareit.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User create(User user) {
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
        return userDao.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User update(User user, int userId) {
        if (!user.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Неправильный формат эл. почты");
        }
        return userDao.update(user, userId);
    }

    public void delete(int userId) {
        userDao.delete(userId);
    }
}
