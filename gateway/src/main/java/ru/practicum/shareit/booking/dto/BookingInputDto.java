package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookingInputDto {
//    @FutureOrPresent(message = "start cant be in past")
//    @NotNull(message = "start time must be not null")
    LocalDateTime start;
//    @FutureOrPresent(message = "end cant be in past")
//    @NotNull(message = "end time must be not null")
    LocalDateTime end;
    Long itemId;
}
