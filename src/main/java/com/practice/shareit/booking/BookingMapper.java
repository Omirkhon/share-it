package com.practice.shareit.booking;

import com.practice.shareit.item.ItemMapper;
import com.practice.shareit.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingReadDto toDto(Booking booking) {
        BookingReadDto bookingReadDto = new BookingReadDto();
        bookingReadDto.setId(booking.getId());
        bookingReadDto.setStart(booking.getStartDate());
        bookingReadDto.setEnd(booking.getEndDate());
        bookingReadDto.setStatus(booking.getStatus());
        bookingReadDto.setItem(itemMapper.toDto(booking.getItem()));
        bookingReadDto.setBooker(userMapper.toDto(booking.getBooker()));

        return bookingReadDto;
    }

    public List<BookingReadDto> toDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toDto)
                .toList();
    }
}
