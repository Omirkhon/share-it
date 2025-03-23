package com.practice.shareit.booking;

import com.practice.shareit.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    public BookingReadDto create(@RequestHeader(RequestConstants.HEADER) int userId, @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        return bookingMapper.toDto(bookingService.create(userId, bookingCreateDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingReadDto updateStatus(@RequestHeader(RequestConstants.HEADER) int userId,
                                       @PathVariable int bookingId,
                                       @RequestParam Boolean approved) {
        return bookingMapper.toDto(bookingService.updateStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingReadDto findById(@PathVariable int bookingId) {
        return bookingMapper.toDto(bookingService.findById(bookingId));
    }

    @GetMapping
    public List<BookingReadDto> findAllByCurrentUser(@RequestHeader(RequestConstants.HEADER) int userId,
                                                     @RequestParam(required = false) String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingMapper.toDto(bookingService.findAllByCurrentUser(userId, state, from, size));
    }

    @GetMapping("/owner")
    public List<BookingReadDto> findAllByOwner(@RequestHeader(RequestConstants.HEADER) int userId,
                                               @RequestParam(required = false) String state,
                                               @RequestParam(defaultValue = "0") @Min(0) int from,
                                               @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingMapper.toDto(bookingService.findAllByOwner(userId, state, from, size));
    }
}
