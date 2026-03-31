package ru.practicum.shareit.user;

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
