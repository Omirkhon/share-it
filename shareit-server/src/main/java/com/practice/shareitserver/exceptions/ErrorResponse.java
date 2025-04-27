package com.practice.shareitserver.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}
