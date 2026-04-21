package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.ItemOutputDto;
import ru.practicum.shareit.user.UserOutputDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingOutputDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingState status;
    ItemOutputDto item;
    UserOutputDto booker;
}
