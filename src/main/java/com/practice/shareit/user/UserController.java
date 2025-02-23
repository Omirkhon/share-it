package com.practice.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @PostMapping
    public UserDto create(@RequestBody User user) {
        return userMapper.toDto(userService.create(user));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userMapper.toDto(userService.findAll());
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable int id) {
        return userMapper.toDto(userService.findById(id));
    }

    @PatchMapping("{userId}")
    public UserDto update(@RequestBody User user, @PathVariable int userId) {
        return userMapper.toDto(userService.update(user, userId));
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }
}
