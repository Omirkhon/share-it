package com.practice.shareit.user;

import jakarta.validation.Valid;
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
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userMapper.toDto(userService.create(userDto));
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
    public UserDto update(@RequestBody UserDto user, @PathVariable int userId) {
        return userMapper.toDto(userService.update(user, userId));
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }
}
