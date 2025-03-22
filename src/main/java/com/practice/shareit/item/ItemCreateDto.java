package com.practice.shareit.item;

import lombok.Data;

@Data
public class ItemCreateDto {
    String name;
    String description;
    Boolean available;
    Integer request_id;
}
