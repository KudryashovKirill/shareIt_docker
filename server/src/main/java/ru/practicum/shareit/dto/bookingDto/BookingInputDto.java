package ru.practicum.shareit.dto.bookingDto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingInputDto {
    @FutureOrPresent(message = "start cant be in past")
    @NotNull(message = "start time must be not null")
    LocalDateTime start;
    @FutureOrPresent(message = "end cant be in past")
    @NotNull(message = "end time must be not null")
    LocalDateTime end;
    Long itemId;
}
