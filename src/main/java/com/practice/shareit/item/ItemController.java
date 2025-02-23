package com.practice.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto itemDto) {
        return itemMapper.toDto(itemService.create(userId, itemDto));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        return itemMapper.toDto(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("{itemId}")
    public ItemDto findById(@PathVariable int itemId) {
        return itemMapper.toDto(itemService.findById(itemId));
    }

    @GetMapping()
    public List<ItemDto> findAllOwnItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemMapper.toDto(itemService.findAllOwnItems(userId));
    }

    @GetMapping("search")
    public List<ItemDto> findByText(@RequestParam String text) {
        return itemMapper.toDto(itemService.findByText(text));
    }
}