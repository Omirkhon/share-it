package com.practice.shareit.request;

import com.practice.shareit.item.ItemDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class RequestReadDto {
    int id;
    String description;
    LocalDateTime created;
    List<ItemDto> items;
}
