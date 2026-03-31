package ru.practicum.shareit.util;

import org.mapstruct.Mapper;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestInputDto;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestOutputDto;
import ru.practicum.shareit.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface ItemRequestMapper {
    ItemRequest toEntity(ItemRequestInputDto dto);

    ItemRequestOutputDto toOutputDto(ItemRequest entity);

    List<ItemRequestOutputDto> toOutputDtoList(List<ItemRequest> requests);
}
