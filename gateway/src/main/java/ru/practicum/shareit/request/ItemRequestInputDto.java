package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemRequestInputDto {
    @NotNull(message = "description must be not null")
    String description;
}
// input output dto возможно(проанализировать тесты)