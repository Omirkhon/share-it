package com.practice.shareitserver.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreateDto {
    @NotNull
    String text;
}
