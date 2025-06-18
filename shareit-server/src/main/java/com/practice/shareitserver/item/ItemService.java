package com.practice.shareitserver.item;

import com.practice.shareitserver.booking.Booking;
import com.practice.shareitserver.booking.BookingRepository;
import com.practice.shareitserver.booking.BookingStatus;
import com.practice.shareitserver.comment.Comment;
import com.practice.shareitserver.comment.CommentCreateDto;
import com.practice.shareitserver.comment.CommentRepository;
import com.practice.shareitserver.exceptions.NotFoundException;
import com.practice.shareitserver.exceptions.ValidationException;
import com.practice.shareitserver.request.RequestRepository;
import com.practice.shareitserver.user.User;
import com.practice.shareitserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    public Item create(int userId, ItemCreateDto itemCreateDto) {
        Item item = new Item();
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getAvailable());
        if (itemCreateDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemCreateDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос не найден")));
        }
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
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

    public Item findById(int userId, int itemId) {
        Item item =  itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена."));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (item.getOwner().getId() == userId) {
            item.setLastBooking(bookingRepository.findFirstByItemIdAndStartDateIsBeforeOrderByEndDateDesc(item.getId(), LocalDateTime.now()).orElse(null));
            item.setNextBooking(bookingRepository.findFirstByItemIdAndStartDateIsAfterOrderByStartDate(item.getId(), LocalDateTime.now()).orElse(null));
        }
        return item;
    }

    public List<Item> findAllOwnItems(int userId, int from, int size) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Item> pageResult = itemRepository.findAllByOwnerOrderById(owner, pageable);
        List<Item> items =  pageResult.getContent();
        for (Item item : items) {
            item.setLastBooking(bookingRepository.findFirstByItemIdAndStartDateIsBeforeOrderByEndDateDesc(item.getId(), LocalDateTime.now()).orElse(null));
            item.setNextBooking(bookingRepository.findFirstByItemIdAndStartDateIsAfterOrderByStartDate(item.getId(), LocalDateTime.now()).orElse(null));
        }
        return items;
    }

    public List<Item> findByText(String text, int from, int size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Item> pageResult = itemRepository.search(text, pageable);
        List<Item> items =  pageResult.getContent();
        for (Item item : items) {
            item.setLastBooking(bookingRepository.findFirstByItemIdAndStartDateIsBeforeOrderByEndDateDesc(item.getId(), LocalDateTime.now()).orElse(null));
            item.setNextBooking(bookingRepository.findFirstByItemIdAndStartDateIsAfterOrderByStartDate(item.getId(), LocalDateTime.now()).orElse(null));
        }
        return items;
    }

    public Comment createComment(int userId, int itemId, CommentCreateDto commentCreateDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findByBookerAndItem(user, item)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ValidationException("Вы не брали данную вещь в аренду"));

        if (!booking.getStatus().equals(BookingStatus.APPROVED) || !booking.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Вы не брали данную вещь в аренду.");
        }

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(commentCreateDto.getText());

        return commentRepository.save(comment);
    }
}
