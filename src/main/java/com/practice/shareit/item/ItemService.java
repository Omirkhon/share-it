package com.practice.shareit.item;

import com.practice.shareit.exception.NotFoundException;
import com.practice.shareit.exception.ValidationException;
import com.practice.shareit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final ItemDao itemDao;

    public Item create(int userId, ItemDto itemDto) {
        if (userService.findById(userId) == null) {
            throw new NotFoundException("Такой пользователь не существует");
        }
        if (itemDto.getName() == null || itemDto.getDescription() == null || itemDto.getAvailable() == null
                || itemDto.getName().isBlank() || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Имя и описание не должны быть пустыми.");
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
