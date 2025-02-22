package com.practice.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    Map<Integer, User> users = new HashMap<>();

    public UserDto create(User user) {
        if (user.getName().isBlank() || user.getEmail().isBlank()) {
            throw new RuntimeException();
        }
        users.put(user.getId(), user);
        return userMapper.toDto(user);
    }

    public List<UserDto> findAll() {
        return userMapper.toDto(users.values().stream().toList());
    }

    public UserDto findById(int id) {
        return userMapper.toDto(users.get(id));
    }

    public UserDto update(int userId, User user) {
        users.put(user.getId(), user);
        return userMapper.toDto(user);
    }
}
