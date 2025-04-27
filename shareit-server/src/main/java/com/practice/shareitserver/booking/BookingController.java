package com.practice.shareitserver.booking;

import com.practice.shareitserver.utils.RequestConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingReadDto create(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingMapper.toDto(bookingService.create(userId, bookingCreateDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingReadDto updateStatus(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                       @PathVariable int bookingId,
                                       @RequestParam Boolean approved) {
        return bookingMapper.toDto(bookingService.updateStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingReadDto findById(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @PathVariable int bookingId) {
        return bookingMapper.toDto(bookingService.findById(userId, bookingId));
    }

    @GetMapping
    public List<BookingReadDto> findAllByCurrentUser(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                                     @RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingMapper.toDto(bookingService.findAllByCurrentUser(userId, state, from, size));
    }

    @GetMapping("/owner")
    public List<BookingReadDto> findAllByOwner(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @Min(0) int from,
                                               @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingMapper.toDto(bookingService.findAllByOwner(userId, state, from, size));
    }
}
