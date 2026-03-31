package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.UserOutputDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutputDto {
    Long id;
    UserOutputDto author;
    String text;
}
