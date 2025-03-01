package com.practice.shareit.booking;

import com.practice.shareit.utils.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BookingReadDto {
    int id;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Status status;
}
