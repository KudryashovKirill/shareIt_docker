package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.user.UserOutputDto;
import ru.practicum.shareit.comment.CommentOutputDto;

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
