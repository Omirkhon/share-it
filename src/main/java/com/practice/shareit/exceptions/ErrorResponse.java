package com.practice.shareit.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message) {
        this.message = message;
    }
}
