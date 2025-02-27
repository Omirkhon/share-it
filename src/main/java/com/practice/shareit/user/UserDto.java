package com.practice.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    int id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email(message = "Некорректный формат эл. почты")
    String email;
}
