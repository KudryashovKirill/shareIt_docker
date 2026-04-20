package ru.practicum.shareit.dto.bookingDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;
import ru.practicum.shareit.model.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingOutputDto implements Serializable {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    ItemOutputDto item;
    UserOutputDto booker;
}
