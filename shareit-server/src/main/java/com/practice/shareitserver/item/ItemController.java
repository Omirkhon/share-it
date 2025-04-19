package com.practice.shareitserver.item;

import com.practice.shareitserver.comment.CommentCreateDto;
import com.practice.shareitserver.comment.CommentMapper;
import com.practice.shareitserver.comment.CommentReadDto;
import com.practice.shareitserver.utils.RequestConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto create(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemMapper.toDto(itemService.create(userId, itemCreateDto));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        return itemMapper.toDto(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("{itemId}")
    public ItemDto findById(@PathVariable int itemId) {
        return itemMapper.toDto(itemService.findById(itemId));
    }

    @GetMapping()
    public List<ItemDto> findAllOwnItems(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return itemMapper.toDto(itemService.findAllOwnItems(userId, from, size));
    }

    @GetMapping("search")
    public List<ItemDto> findByText(@RequestParam String text,
                                    @RequestParam(defaultValue = "0") @Min(0) int from,
                                    @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        return itemMapper.toDto(itemService.findByText(text, from, size));
    }

    @PostMapping("{itemId}/comment")
    public CommentReadDto createComment(@RequestHeader(RequestConstants.USER_ID_HEADER) int userId,
                                        @PathVariable int itemId,
                                        @RequestBody CommentCreateDto commentCreateDto) {
        return commentMapper.toDto(itemService.createComment(userId, itemId, commentCreateDto));
    }
}
