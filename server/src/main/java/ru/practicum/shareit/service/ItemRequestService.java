package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.itemRequestDto.ItemRequestInputDto;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestOutputDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutputDto createItemRequest(ItemRequestInputDto dto, Long requestorId);

    List<ItemRequestOutputDto> getRequestsWithAnswers(Long requestorId);

    List<ItemRequestOutputDto> getRequestsByOtherUsers(Long from, Long size, Long userId);

    ItemRequestOutputDto getRequest(Long requestorId, Long requestId);
}
