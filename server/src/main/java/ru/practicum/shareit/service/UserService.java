package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.userDto.UserInputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserOutputDto create(UserInputDto userDto);

    UserOutputDto update(Long id, UserInputDto userDto);

    UserOutputDto getById(Long id);

    List<UserOutputDto> getAll();

    Map<String, Boolean> delete(Long id);
}
