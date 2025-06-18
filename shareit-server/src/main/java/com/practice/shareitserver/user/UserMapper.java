package com.practice.shareitserver.user;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public List<UserDto> toDto(List<User> items) {
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}
