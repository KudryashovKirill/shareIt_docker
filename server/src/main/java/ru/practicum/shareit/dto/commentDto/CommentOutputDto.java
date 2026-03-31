package ru.practicum.shareit.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.dto.userDto.UserOutputDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutputDto {
    Long id;
    UserOutputDto author;
    String text;
}
