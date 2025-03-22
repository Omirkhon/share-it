package com.practice.shareit.item;

import com.practice.shareit.comment.CommentCreateDto;
import com.practice.shareit.comment.CommentMapper;
import com.practice.shareit.comment.CommentReadDto;
import com.practice.shareit.utils.RequestConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto create(@RequestHeader(RequestConstants.HEADER) int userId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemMapper.toDto(itemService.create(userId, itemCreateDto));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader(RequestConstants.HEADER) int userId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        return itemMapper.toDto(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("{itemId}")
    public ItemDto findById(@PathVariable int itemId) {
        return itemMapper.toDto(itemService.findById(itemId));
    }

    @GetMapping()
    public List<ItemDto> findAllOwnItems(@RequestHeader(RequestConstants.HEADER) int userId) {
        return itemMapper.toDto(itemService.findAllOwnItems(userId));
    }

    @GetMapping("search")
    public List<ItemDto> findByText(@RequestParam String text) {
        return itemMapper.toDto(itemService.findByText(text));
    }

    @PostMapping("{itemId}/comment")
    public CommentReadDto createComment(@RequestHeader(RequestConstants.HEADER) int userId,
                                        @PathVariable int itemId,
                                        @RequestBody CommentCreateDto commentCreateDto) {
        return commentMapper.toDto(itemService.createComment(userId, itemId, commentCreateDto));
    }
}
