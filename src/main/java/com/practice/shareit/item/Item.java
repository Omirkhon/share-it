package com.practice.shareit.item;

import com.practice.shareit.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
public class Item {
    int id;
    String name;
    String description;
    Boolean available;
    User owner;
}
