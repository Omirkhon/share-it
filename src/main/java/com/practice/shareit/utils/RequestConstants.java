package com.practice.shareit.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;

@UtilityClass
@Getter
@Service
public class RequestConstants {
    private final String header = "X-Sharer-User-Id";
}
