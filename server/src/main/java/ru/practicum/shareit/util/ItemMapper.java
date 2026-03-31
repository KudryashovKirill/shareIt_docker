package ru.practicum.shareit.util;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.dto.itemDto.ItemInputDto;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;
import ru.practicum.shareit.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemOutputDto toOutputDto(Item item);

    Item toEntity(ItemInputDto dto);
}

