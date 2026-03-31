package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;


@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingInputDto dto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Creating booking {}, userId={}", dto, userId);
        return bookingClient.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam Boolean approved,
                                          @PathVariable Long bookingId) {

        log.info("Approve booking {}, approved={}, userId={}", bookingId, approved, userId);
        return bookingClient.approve(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long bookingId) {

        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingByBooker(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Get bookings by owner, userId={}", userId);
        return bookingClient.getBookingByOwner(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Get bookings by booker, userId={}", userId);
        return bookingClient.getAllUsersBookings(userId);
    }
}


//	@GetMapping
//	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestParam(name = "state", defaultValue = "all") String stateParam,
//			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
//			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
//		BookingState state = BookingState.from(stateParam)
//				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
//		return bookingClient.getBookings(userId, state, from, size);
//	}
//
//	@PostMapping
//	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestBody @Valid BookItemRequestDto requestDto) {
//		log.info("Creating booking {}, userId={}", requestDto, userId);
//		return bookingClient.bookItem(userId, requestDto);
//	}
//
//	@GetMapping("/{bookingId}")
//	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
//			@PathVariable Long bookingId) {
//		log.info("Get booking {}, userId={}", bookingId, userId);
//		return bookingClient.getBooking(userId, bookingId);
//	}

