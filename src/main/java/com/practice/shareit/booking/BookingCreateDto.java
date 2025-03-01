package com.practice.shareit.booking;

import com.practice.shareit.item.Item;
import com.practice.shareit.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BookingCreateDto {
    int id;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Item item;
    User booker;
}
