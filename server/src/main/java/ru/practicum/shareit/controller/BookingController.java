package ru.practicum.shareit.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.bookingDto.BookingInputDto;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;
import ru.practicum.shareit.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingOutputDto> create(@RequestBody @Valid BookingInputDto dto,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.create(dto, userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingOutputDto> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam Boolean approved,
                                                    @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.approve(bookingId, userId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutputDto> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.getBookingByBooker(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping("owner")
    public ResponseEntity<List<BookingOutputDto>> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.getAllUsersBookings(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingOutputDto>> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.getAllUsersBookings(userId), HttpStatus.OK);
    }
}
