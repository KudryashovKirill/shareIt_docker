package ru.practicum.shareit.dto.userDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserOutputDto {
    Long id;
    String name;
    String email;
}
