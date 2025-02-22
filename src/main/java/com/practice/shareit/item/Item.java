package com.practice.shareit.item;

import com.practice.shareit.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    int id;
    String name;
    String description;
    Boolean available;
    User owner;
}
