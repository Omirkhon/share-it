package com.practice.shareit.item;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.exceptions.ValidationException;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserService;
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
    private final UserService userService;
    private final ItemDao itemDao;

    public Item create(int userId, ItemDto itemDto) {
        if (userService.findById(userId) == null) {
            throw new NotFoundException("Такой пользователь не существует");
        }
        return itemDao.create(userId, itemDto);
    }

    public Item update(int userId, int itemId, ItemDto itemDto) {
        return itemDao.update(userId, itemId, itemDto);
    }

    public Item findById(int itemId) {
        return itemDao.findById(itemId);
    }

    public List<Item> findAllOwnItems(int userId) {
        return itemDao.findAllOwnItems(userId);
    }

    public List<Item> findByText(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemDao.findByText(text);
    }
}
