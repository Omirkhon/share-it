package com.practice.shareit.item;

import com.practice.shareit.booking.Booking;
import com.practice.shareit.booking.BookingRepository;
import com.practice.shareit.booking.Status;
import com.practice.shareit.comment.Comment;
import com.practice.shareit.comment.CommentCreateDto;
import com.practice.shareit.comment.CommentRepository;
import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.exceptions.ValidationException;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public Item create(int userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    public Item update(int userId, int itemId, ItemDto itemDto) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена."));
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
        return itemRepository.save(oldItem);
    }

    public Item findById(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена."));
    }

    public List<Item> findAllOwnItems(int userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return itemRepository.findAllByOwner(owner);
    }

    public List<Item> findByText(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    public Comment createComment(int userId, int itemId, CommentCreateDto commentCreateDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findBookingByBookerAndItem(user, item).orElseThrow(() -> new ValidationException(""));

        if (!booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Вы не брали данную вещь в аренду.");
        }

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(commentCreateDto.getText());

        return commentRepository.save(comment);
    }
}
