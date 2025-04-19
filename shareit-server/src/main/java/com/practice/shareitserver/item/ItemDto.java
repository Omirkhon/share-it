package com.practice.shareitserver.item;

import com.practice.shareitserver.comment.CommentReadDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ItemDto {
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    String description;
    @NotNull(message = "Статус наличия не указан")
    Boolean available;
    final List<CommentReadDto> comments = new ArrayList<>();
    Integer requestId;
}
