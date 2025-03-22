package com.practice.shareit.request;

import com.practice.shareit.utils.RequestConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public RequestReadDto create(@RequestHeader(RequestConstants.HEADER) int userId, @RequestBody RequestCreateDto requestCreateDto) {
        return requestMapper.toDto(requestService.create(userId, requestCreateDto));
    }

    @GetMapping
    public List<RequestReadDto> findAllByUser(@RequestHeader(RequestConstants.HEADER) int userId) {
        return requestMapper.toDto(requestService.findAllByUser(userId));
    }

    @GetMapping("{id}")
    public RequestReadDto findById(@RequestHeader(RequestConstants.HEADER) int userId, @PathVariable int id) {
        return requestMapper.toDto(requestService.findById(userId, id));
    }

    @GetMapping("/all")
    public List<RequestReadDto> findByPageAndSize(@RequestHeader(RequestConstants.HEADER) int userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                                  @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return requestMapper.toDto(requestService.findByPageAndSize(userId, from, size));
    }
}