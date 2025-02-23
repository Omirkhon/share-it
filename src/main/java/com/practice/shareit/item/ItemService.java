package com.practice.shareit.item;

import com.practice.shareit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;
    private final Map<Integer, Item> items = new HashMap<>();

    public ItemDto create(Item item) {
        items.put(item.getId(), item);
        return itemMapper.toDto(item);
    }

    public ItemDto update(int itemId, Item item) {
        if (itemId != item.getId()) {
            throw new RuntimeException();
        }
        items.put(item.getId(), item);
        return itemMapper.toDto(item);
    }

    public ItemDto findById(int itemId) {
        return itemMapper.toDto(items.get(itemId));
    }

    public List<ItemDto> findAllOwnItems(User owner) { // не owner, а userId
        List<Item> ownItems = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            if (entry.getValue().getOwner() == owner) {
                ownItems.add(entry.getValue());
            }
        }
        return itemMapper.toDto(ownItems);
    }

    public List<ItemDto> findByText(String text) {
        List<Item> similarItems = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            if (entry.getValue().getName().contains(text) || entry.getValue().getDescription().contains(text)) {
                similarItems.add(entry.getValue());
            }
        }
        return itemMapper.toDto(similarItems);
    }
}
