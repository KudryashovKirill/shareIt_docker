package ru.practicum.shareit.util;

import org.mapstruct.Mapper;
import ru.practicum.shareit.dto.userDto.UserInputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;
import ru.practicum.shareit.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserInputDto dto);

    UserOutputDto toOutputDto(User user);
}
