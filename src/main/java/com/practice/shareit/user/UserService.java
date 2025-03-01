package com.practice.shareit.user;

import com.practice.shareit.exceptions.AuthorisationException;
import com.practice.shareit.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            throw new AuthorisationException("Пользователь с такой эл. почтой уже существует");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User update(User user, int userId) {
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (oldUser.getEmail().equals(user.getEmail())) {
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
        } else if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new AuthorisationException("Пользователь с такой эл. почтой уже существует");
        } else {
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                oldUser.setEmail(user.getEmail());
            }
        }
        return userRepository.save(oldUser);
    }

    public void delete(int userId) {
        userRepository.deleteById(userId);
    }
}
