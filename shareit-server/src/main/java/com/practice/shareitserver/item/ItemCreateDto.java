package com.practice.shareitserver.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCreateDto {
    String name;
    String description;
    Boolean available;
    Integer requestId;
}
