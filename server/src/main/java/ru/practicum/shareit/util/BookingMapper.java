package ru.practicum.shareit.util;

import org.mapstruct.Mapper;
import ru.practicum.shareit.dto.bookingDto.BookingInputDto;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;
import ru.practicum.shareit.model.Booking;

import java.util.List;


@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {
    Booking toEntity(BookingInputDto dto);

    BookingOutputDto toDto(Booking booking);

    List<BookingOutputDto> toDtoList(List<Booking> bookings);

}
