package com.practice.shareitgateway.item;

import com.practice.shareitserver.comment.CommentCreateDto;
import com.practice.shareitserver.item.ItemCreateDto;
import com.practice.shareitserver.item.ItemDto;
import com.practice.shareitserver.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/items")
@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemClient.create(userId, itemCreateDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> findById(@PathVariable int itemId) {
        return itemClient.findById(itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> findAllOwnItems(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return itemClient.findAllOwnItems(userId, from, size);
    }

    @GetMapping("search")
    public ResponseEntity<Object> findByText(@RequestParam String text,
                                    @RequestParam(defaultValue = "0") @Min(0) int from,
                                    @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return itemClient.findByText(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                        @PathVariable int itemId,
                                        @RequestBody CommentCreateDto commentCreateDto) {
        return itemClient.createComment(userId, itemId, commentCreateDto);
    }
}
