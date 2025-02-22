package com.practice.shareit.item;

import lombok.Data;

@Data
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
}
