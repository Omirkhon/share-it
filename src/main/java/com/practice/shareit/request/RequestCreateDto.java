package com.practice.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCreateDto {
    @NotBlank(message = "Описание не может быть пустым")
    String description;
}
