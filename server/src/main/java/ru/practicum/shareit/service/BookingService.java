package ru.practicum.shareit.service;

import java.util.List;

import ru.practicum.shareit.dto.bookingDto.BookingInputDto;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;

public interface BookingService {
    BookingOutputDto create(BookingInputDto dto, Long xSharerUserId);

    BookingOutputDto approve(Long bookingId, Long userId, Boolean approved);

    BookingOutputDto getBookingByBooker(Long bookingId, Long userId);

    List<BookingOutputDto> getAllUsersBookings(Long userId);
}
