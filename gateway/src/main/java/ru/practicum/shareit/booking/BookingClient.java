package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Object> create(BookingInputDto bookingInputDto, Long userId) {
        return post("", userId, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(Long userId,
                                          Boolean approved,
                                          Long bookingId) {
        Map<String, Object> params = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", userId, params, null);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByBooker(Long userId,
                                                     Long bookingId) {
        return get("/" + bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(Long userId) {
        return get("/owner", userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBookings(Long userId) {
        return get("", userId);
    }

}
