package com.practice.shareit.comment;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {
    public CommentReadDto toDto(Comment comment) {
        CommentReadDto commentReadDto = new CommentReadDto();
        commentReadDto.setId(comment.getId());
        commentReadDto.setText(comment.getText());
        commentReadDto.setAuthorName(comment.getAuthor().getName());
        commentReadDto.setCreated(comment.getCreated());

        return commentReadDto;
    }

    public List<CommentReadDto> toDto(List<Comment> comments) {
        return comments.stream()
                .map(this::toDto)
                .toList();
    }
}
