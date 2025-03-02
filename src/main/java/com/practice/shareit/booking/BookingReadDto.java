package com.practice.shareit.booking;

import com.practice.shareit.item.ItemDto;
import com.practice.shareit.user.UserDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BookingReadDto {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserDto booker;
    ItemDto item;
}
