package ru.practicum.shareit.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestInputDto;
import ru.practicum.shareit.dto.itemRequestDto.ItemRequestOutputDto;
import ru.practicum.shareit.model.ItemRequest;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.ItemRepositoryJpa;
import ru.practicum.shareit.repository.ItemRequestRepositoryJpa;
import ru.practicum.shareit.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.ItemMapper;
import ru.practicum.shareit.util.ItemRequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final ItemRepositoryJpa itemRepository;
    private final UserRepositoryJpa userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepositoryJpa itemRequestRepository,
                                  ItemRepositoryJpa itemRepository,
                                  UserRepositoryJpa userRepository,
                                  ItemRequestMapper itemRequestMapper,
                                  ItemMapper itemMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestMapper = itemRequestMapper;
        this.itemMapper = itemMapper;
    }

    @Transactional
    @Override
    public ItemRequestOutputDto createItemRequest(ItemRequestInputDto dto, Long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NoSuchElementException("no user found by id"));
        ItemRequest request = itemRequestMapper.toEntity(dto);
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        request = itemRequestRepository.save(request);
        return itemRequestMapper.toOutputDto(request);
    }

    @Override
    public List<ItemRequestOutputDto> getRequestsWithAnswers(Long requestorId) {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        return requests.stream()
                .map(this::mapWithAnswers)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestOutputDto> getRequestsByOtherUsers(Long from, Long size, Long userId) {
        Pageable pageable = PageRequest.of(from.intValue() / size.intValue(),
                size.intValue(), Sort.by("created").descending());
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNot(userId, pageable);
        return requests.stream()
                .map(this::mapWithAnswers)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestOutputDto getRequest(Long requestorId, Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        userRepository.findById(requestorId).orElseThrow(() ->
                new NoSuchElementException("User not found"));
        return mapWithAnswers(request);
    }

    private ItemRequestOutputDto mapWithAnswers(ItemRequest request) {
        ItemRequestOutputDto dto = itemRequestMapper.toOutputDto(request);
        List<ItemOutputDto> answers = itemRepository.findAllByRequestId(request.getId())
                .stream()
                .map(itemMapper::toOutputDto)
                .collect(Collectors.toList());
        dto.setAnswers(answers);
        return dto;
    }
}
