package ru.practicum.shareit.dto.userDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInputDto {
    @NotNull(message = "name must be not null")
    String name;
    @NotNull(message = "email must be not null")
    String email;
}
