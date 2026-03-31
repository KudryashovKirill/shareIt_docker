package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.ItemOutputDto;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestOutputDto {
    Long id;
    String description;
    Long requestorId;
    LocalDateTime created;
    List<ItemOutputDto> answers;
}
