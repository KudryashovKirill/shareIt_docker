package ru.practicum.shareit.dto.userDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserOutputDto implements Serializable {
    Long id;
    String name;
    String email;
}
