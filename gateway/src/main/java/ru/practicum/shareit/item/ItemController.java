package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentInputDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemInputDto itemInputDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Creating item {}, userId={}", itemInputDto, userId);
        return itemClient.create(itemInputDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentInputDto commentInputDto,
                                             @PathVariable Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Add comment to item {}, userId={}", itemId, userId);
        return itemClient.addComment(commentInputDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemInputDto itemInputDto,
                                         @PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Updating item {}, userId={}", itemId, userId);
        return itemClient.update(itemInputDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("Get items by owner {}, from={}, size={}", userId, from, size);
        return itemClient.findAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam String text,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("Search items text={}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.search(text, userId, from, size);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable Long itemId) {

        log.info("Delete item {}", itemId);
        return itemClient.delete(itemId);
    }
}