package com.practice.shareitgateway.user;

import com.practice.shareitserver.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        return userClient.create(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return userClient.findById(id);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto user, @PathVariable int userId) {
        return userClient.update(user, userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable int userId) {
        return userClient.delete(userId);
    }
}
