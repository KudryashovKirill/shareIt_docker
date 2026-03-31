package ru.practicum.shareit.dto.itemDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemInputDto {
    @NotNull(message = "name of item cant be null")
    @NotBlank(message = "name of item cant be blank")
    String name;
    @NotNull(message = "description of item cant be null")
    @NotBlank(message = "description of item cant be blank")
    String description;
    @NotNull(message = "item should be available or not, not null")
    Boolean available;
    Long requestId;
}
