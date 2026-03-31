package ru.practicum.shareit.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.dto.commentDto.CommentInputDto;
import ru.practicum.shareit.dto.itemDto.ItemInputDto;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;

import java.util.List;
import java.util.Map;

public interface ItemService {

    ItemOutputDto create(ItemInputDto dto, Long userId);

    ItemOutputDto update(Long itemId, ItemInputDto dto, Long userId);

    ItemOutputDto addComment(Long itemId, Long userId, CommentInputDto commentInputDto);

    ItemOutputDto getById(Long itemId, Long userId);

    List<ItemOutputDto> getAllByOwner(Long ownerId, Pageable pageable);

    List<ItemOutputDto> search(String text, Pageable pageable);

    Map<String, Boolean> delete(Long itemId);
}
