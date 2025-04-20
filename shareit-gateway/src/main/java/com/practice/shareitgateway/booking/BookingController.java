package com.practice.shareitgateway.booking;

import com.practice.shareitserver.booking.BookingCreateDto;
import com.practice.shareitserver.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient bookingClient;
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        return bookingClient.create(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                       @PathVariable int bookingId,
                                       @RequestParam Boolean approved) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable int bookingId) {
        return bookingClient.findById(bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByCurrentUser(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                                     @RequestParam(required = false) String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingClient.findAllByCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                               @RequestParam(required = false) String state,
                                               @RequestParam(defaultValue = "0") @Min(0) int from,
                                               @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return bookingClient.findAllByOwner(userId, state, from, size);
    }
}
