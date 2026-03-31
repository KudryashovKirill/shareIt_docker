package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping("/request")
    public ResponseEntity<Object> createItemRequest(
            @RequestBody @Valid ItemRequestInputDto dto,
            @RequestHeader("X-Sharer-User-Id") Long requestorId) {

        log.info("Creating item request {}, userId={}", dto, requestorId);
        return requestClient.createItemRequest(dto, requestorId);
    }

    @GetMapping("/request")
    public ResponseEntity<Object> getRequestsWithAnswers(
            @RequestHeader("X-Sharer-User-Id") Long requestorId) {

        log.info("Get requests of user {}", requestorId);
        return requestClient.getRequestsWithAnswers(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam Long from,
            @RequestParam Long size) {

        log.info("Get requests by other users, userId={}, from={}, size={}", userId, from, size);
        return requestClient.getRequestsByOtherUsers(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(
            @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @PathVariable Long id) {

        log.info("Get request {}, userId={}", id, requestorId);
        return requestClient.getRequest(requestorId, id);
    }
}
