package com.practice.shareit.booking;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {
    public BookingReadDto toDto(Booking booking) {
        BookingReadDto bookingReadDto = new BookingReadDto();
        bookingReadDto.setId(booking.getId());
        bookingReadDto.setStartDate(booking.getStartDate());
        bookingReadDto.setEndDate(booking.getEndDate());
        bookingReadDto.setStatus(booking.getStatus());

        return bookingReadDto;
    }

    public List<BookingReadDto> toDto(List<Booking> items) {
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}
