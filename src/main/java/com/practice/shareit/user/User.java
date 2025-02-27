package com.practice.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {
    int id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email (message = "Некорректный формат эл. почты")
    String email;
}
