package com.practice.shareitserver.user;

import com.practice.shareitserver.exceptions.ConflictException;
import com.practice.shareitserver.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            throw new ConflictException("Пользователь с такой эл. почтой уже существует");
        }
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User update(UserDto user, int userId) {
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail != null && userByEmail.getId() != userId) {
            throw new ConflictException("Пользователь с такой эл. почтой уже существует");
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (oldUser.getEmail().equals(user.getEmail())) {
            return userRepository.save(oldUser);
        } else if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        return userRepository.save(oldUser);
    }

    public void delete(int userId) {
        userRepository.deleteById(userId);
    }
}
