package com.practice.shareit.comment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class CommentReadDto {
    int id;
    String text;
    String authorName;
    LocalDateTime created;
}
