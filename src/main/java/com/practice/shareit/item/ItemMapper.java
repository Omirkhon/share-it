package com.practice.shareit.item;

import com.practice.shareit.comment.Comment;
import com.practice.shareit.comment.CommentMapper;
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
        for (Comment comment : item.getComments()) {
            itemDto.getComments().add(commentMapper.toDto(comment));
        }

        return itemDto;
    }

    public List<ItemDto> toDto(List<Item> items) {
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}
