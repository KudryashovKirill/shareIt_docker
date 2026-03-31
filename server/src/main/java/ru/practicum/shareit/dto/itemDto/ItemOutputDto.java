package ru.practicum.shareit.dto.itemDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;
import ru.practicum.shareit.dto.commentDto.CommentOutputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemOutputDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
    UserOutputDto owner;
    BookingOutputDto lastBooking;
    BookingOutputDto nextBooking;
    List<CommentOutputDto> comments;
}
