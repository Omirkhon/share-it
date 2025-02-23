package com.practice.shareit.item;

import com.practice.shareit.exception.NotFoundException;
import com.practice.shareit.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ItemDao {
    private final UserDao userDao;
    private final Map<Integer, Item> items = new HashMap<>();
    private int uniqueId = 0;

    public Item create(int userId, ItemDto itemDto) {
        int id = ++uniqueId;
        Item item = new Item(id, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), userDao.findById(userId));
        items.put(id, item);
        return item;
    }

    public Item update(int userId, int itemId, ItemDto itemDto) {
        Item oldItem = items.get(itemId);
        if (userId != oldItem.getOwner().getId()) {
            throw new NotFoundException("У вас нет такой вещи");
        }
        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        items.put(itemId, oldItem);
        return oldItem;
    }

    public Item findById(int itemId) {
        return items.get(itemId);
    }

    public List<Item> findAllOwnItems(int userId) {
        List<Item> ownItems = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            if (entry.getValue().getOwner().getId() == userId) {
                ownItems.add(entry.getValue());
            }
        }
        return ownItems;
    }

    public List<Item> findByText(String text) {
        List<Item> similarItems = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            if ((StringUtils.containsIgnoreCase(entry.getValue().getName(), text) ||
                    StringUtils.containsIgnoreCase(entry.getValue().getDescription(), text)) &&
            entry.getValue().getAvailable()) {
                similarItems.add(entry.getValue());
            }
        }
        return similarItems;
    }
}
