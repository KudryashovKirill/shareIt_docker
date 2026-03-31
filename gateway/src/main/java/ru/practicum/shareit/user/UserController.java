package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserInputDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserInputDto userDto,
                                         @PathVariable Long userId) {
        log.info("Updating user {}, {}", userId, userDto);
        return userClient.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Get all users");
        return userClient.findAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Delete user {}", userId);
        return userClient.delete(userId);
    }
}