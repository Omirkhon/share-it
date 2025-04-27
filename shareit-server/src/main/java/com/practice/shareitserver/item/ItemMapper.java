package com.practice.shareitserver.item;

import com.practice.shareitserver.booking.BookingReadDto;
import com.practice.shareitserver.comment.Comment;
import com.practice.shareitserver.comment.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {
    private final CommentMapper commentMapper;
    public ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        if (item.getLastBooking() != null) {
            BookingReadDto lastBooking = new BookingReadDto();
            lastBooking.setId(item.getLastBooking().getId());
            lastBooking.setStatus(item.getLastBooking().getStatus());
            lastBooking.setStart(item.getLastBooking().getStartDate());
            lastBooking.setEnd(item.getLastBooking().getEndDate());
            lastBooking.setBookerId(item.getLastBooking().getBooker().getId());
            itemDto.setLastBooking(lastBooking);
        }

        if (item.getNextBooking() != null) {
            BookingReadDto nextBooking = new BookingReadDto();
            nextBooking.setId(item.getNextBooking().getId());
            nextBooking.setStatus(item.getNextBooking().getStatus());
            nextBooking.setStart(item.getNextBooking().getStartDate());
            nextBooking.setEnd(item.getNextBooking().getEndDate());
            nextBooking.setBookerId(item.getNextBooking().getBooker().getId());
            itemDto.setNextBooking(nextBooking);
        }

        if (!item.getComments().isEmpty()) {
            for (Comment comment : item.getComments()) {
                itemDto.getComments().add(commentMapper.toDto(comment));
            }
        }
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    public List<ItemDto> toDto(List<Item> items) {
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}
