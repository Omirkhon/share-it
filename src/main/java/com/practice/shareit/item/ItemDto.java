package com.practice.shareit.item;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    String description;
    @NotNull(message = "Статус наличия не указан")
    Boolean available;
}
