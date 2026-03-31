package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.userDto.UserInputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;
import ru.practicum.shareit.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserOutputDto> create(@RequestBody @Valid UserInputDto userDto) {
        UserOutputDto createdUser = userService.create(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserOutputDto> update(@RequestBody UserInputDto userDto, @PathVariable Long userId) {
        UserOutputDto updatedUser = userService.update(userId, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserOutputDto> getById(@PathVariable Long userId) {
        UserOutputDto findUser = userService.getById(userId);
        return new ResponseEntity<>(findUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDto>> findAll() {
        List<UserOutputDto> allUsers = userService.getAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.delete(userId), HttpStatus.OK);
    }
}
