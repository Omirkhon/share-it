package com.practice.shareit.item;

import com.practice.shareit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    public ItemDto create(@RequestBody Item item) {
        return itemService.create(item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable int itemId, @RequestBody Item item) {
        return itemService.update(itemId, item);
    }

    @GetMapping("{itemId}")
    public ItemDto findById(@PathVariable int itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping()
    public List<ItemDto> findAllOwnItems(@RequestBody User owner) {
        return itemService.findAllOwnItems(owner);
    }

    @GetMapping("search")
    public List<ItemDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }
}
