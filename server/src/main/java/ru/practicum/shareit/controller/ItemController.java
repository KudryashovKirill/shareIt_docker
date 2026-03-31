package ru.practicum.shareit.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.commentDto.CommentInputDto;
import ru.practicum.shareit.dto.itemDto.ItemInputDto;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;
import ru.practicum.shareit.service.ItemService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemOutputDto> create(@RequestBody @Valid ItemInputDto itemInputDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemOutputDto createdItem = itemService.create(itemInputDto, userId);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<ItemOutputDto> addComment(@RequestBody @Valid CommentInputDto commentInputDto,
                                                    @PathVariable Long itemId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemOutputDto itemWithComment = itemService.addComment(itemId, userId, commentInputDto);
        return new ResponseEntity<>(itemWithComment, HttpStatus.OK);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> update(@RequestBody ItemInputDto itemInputDto,
                                                @PathVariable Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemOutputDto updatedItem = itemService.update(itemId, itemInputDto, userId);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> getById(@PathVariable Long itemId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemOutputDto findItem = itemService.getById(itemId, userId);
        return new ResponseEntity<>(findItem, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemOutputDto>> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemOutputDto> allItems = itemService.getAllByOwner(userId, pageable);

        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemOutputDto>> search(
            @RequestParam String text,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemOutputDto> items = itemService.search(text, pageable);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.delete(itemId), HttpStatus.OK);
    }
}
