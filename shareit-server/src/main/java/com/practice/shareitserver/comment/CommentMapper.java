package com.practice.shareitserver.comment;

import org.springframework.stereotype.Component;

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
}
