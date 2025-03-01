package com.practice.shareit.booking;

import com.practice.shareit.utils.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking create(@RequestHeader(RequestConstants.HEADER) int userId, BookingCreateDto bookingCreateDto) {
        return bookingService.create(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateStatus(@PathVariable int bookingId, @RequestBody Boolean approved) {
        return bookingService.updateStatus(bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingReadDto findById(@PathVariable int bookingId) {
        return bookingMapper.toDto(bookingService.findById(bookingId));
    }

    @GetMapping
    public List<BookingReadDto> findAllByCurrentUser(@RequestHeader(RequestConstants.HEADER) int userId, @RequestBody String state) {
        return bookingMapper.toDto(bookingService.findAllByCurrentUser(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingReadDto> findAllByCurrentUserItems(@RequestHeader(RequestConstants.HEADER) int userId, @RequestBody String state) {
        return bookingMapper.toDto(bookingService.findAllByCurrentUserItems(userId, state));
    }
}
