package com.practice.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @PostMapping
    public UserDto create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PatchMapping("{itemId}")
    public UserDto update(@PathVariable int itemId, @RequestBody User user) {
        return userService.update(itemId, user);
    }
}
