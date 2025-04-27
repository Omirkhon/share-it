package com.practice.shareitgateway.request;

import com.practice.shareitserver.request.RequestCreateDto;
import com.practice.shareitserver.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestClient requestClient;
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @RequestBody @Valid RequestCreateDto requestCreateDto) {
        return requestClient.create(userId, requestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId) {
        return requestClient.findAllByUser(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @PathVariable int id) {
        return requestClient.findById(userId, id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findByPageAndSize(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                                  @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return requestClient.findByPageAndSize(userId, from, size);
    }
}