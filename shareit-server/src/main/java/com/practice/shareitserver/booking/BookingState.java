package com.practice.shareitserver.booking;

import com.practice.shareitserver.exceptions.ValidationException;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState from(String value) {
        for (BookingState bookingState : values()) {
            if (bookingState.name().equals(value)) {
                return bookingState;
            }
        }
        throw new ValidationException("Unknown state: " + value);
    }
}
