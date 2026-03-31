package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestInputDto;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestOutputDto;
import ru.practicum.shareit.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public RequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping("/request")
    public ResponseEntity<ItemRequestOutputDto> createItemRequest(@RequestBody @Valid ItemRequestInputDto dto,
                                                                  @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return new ResponseEntity<>(itemRequestService.createItemRequest(dto, requestorId), HttpStatus.CREATED);
    }

    @GetMapping("/request")
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsWithAnswers(@RequestHeader("X-Sharer-User-Id")
                                                                             Long requestorId) {
        return new ResponseEntity<>(itemRequestService.getRequestsWithAnswers(requestorId), HttpStatus.OK);
    }

    @GetMapping("requests/all")
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsByOtherUsers(@RequestParam Long from,
                                                                              @RequestParam Long size,
                                                                              @RequestHeader("X-Sharer-User-Id")
                                                                              Long userId) {
        return new ResponseEntity<>(itemRequestService.getRequestsByOtherUsers(from, size, userId), HttpStatus.OK);
    }

    @GetMapping("requests/{id}")
    public ResponseEntity<ItemRequestOutputDto> getRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                           @PathVariable Long id) {
        return new ResponseEntity<>(itemRequestService.getRequest(requestorId, id), HttpStatus.OK);
    }
}
