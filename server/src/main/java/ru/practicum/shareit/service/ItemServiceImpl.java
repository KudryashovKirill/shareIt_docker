package ru.practicum.shareit.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.commentDto.CommentInputDto;
import ru.practicum.shareit.dto.itemDto.ItemInputDto;
import ru.practicum.shareit.dto.itemDto.ItemOutputDto;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.BookingRepositoryJpa;
import ru.practicum.shareit.repository.CommentRepositoryJpa;
import ru.practicum.shareit.repository.ItemRepositoryJpa;
import ru.practicum.shareit.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.BookingMapper;
import ru.practicum.shareit.util.CommentMapper;
import ru.practicum.shareit.util.ItemMapper;
import ru.practicum.shareit.util.exception.IllegalOwnerException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemServiceImpl(ItemRepositoryJpa itemRepositoryJpa,
                           UserRepositoryJpa userRepositoryJpa,
                           ItemMapper itemMapper,
                           BookingRepositoryJpa bookingRepositoryJpa,
                           BookingMapper bookingMapper,
                           CommentRepositoryJpa commentRepositoryJpa,
                           CommentMapper commentMapper) {
        this.itemRepositoryJpa = itemRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
        this.itemMapper = itemMapper;
        this.bookingRepositoryJpa = bookingRepositoryJpa;
        this.bookingMapper = bookingMapper;
        this.commentRepositoryJpa = commentRepositoryJpa;
        this.commentMapper = commentMapper;
    }

    @Transactional
    @Override
    @CacheEvict(value = "items_list", allEntries = true)
    public ItemOutputDto create(ItemInputDto dto, Long userId) {
        User owner = checkUserIsInTable(userId);
        Item item = itemMapper.toEntity(dto);
        item.setOwner(owner);
        itemRepositoryJpa.save(item);
        return itemMapper.toOutputDto(item);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "items", allEntries = true),
            @CacheEvict(value = "items_list", allEntries = true)
    })
    public ItemOutputDto update(Long itemId, ItemInputDto dto, Long userId) {
        Item item = checkItemIsInTable(itemId);
        validateIsUserOwnerOfItem(item, userId);
        updateItemFromDto(dto, item);
        return itemMapper.toOutputDto(item);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "items", allEntries = true),
            @CacheEvict(value = "items_list", allEntries = true)
    })
    public ItemOutputDto addComment(Long itemId, Long userId, CommentInputDto commentInputDto) {
        Item item = checkItemIsInTable(itemId);
        User user = checkUserIsInTable(userId);

        checkPastApprovedBooking(userId, itemId);

        Comment comment = commentMapper.toEntity(commentInputDto);
        addParametersToComment(comment, commentInputDto, item, user);
        commentRepositoryJpa.save(comment);

        ItemOutputDto dto = itemMapper.toOutputDto(item);
        setCommentToDto(dto, itemId);

        if (item.getOwner() != null && item.getOwner().getId().equals(userId)) {
            setLastAndNextBookingToDto(dto, itemId);
        }
        return dto;
    }

    @Override
    @Cacheable(value = "items", key = "#itemId + '_' + #userId")
    public ItemOutputDto getById(Long itemId, Long userId) {
        System.out.println("Получен из базы");
        Item item = checkItemIsInTable(itemId);

        ItemOutputDto itemOutputDto = itemMapper.toOutputDto(item);

        setCommentToDto(itemOutputDto, itemId);

        if (item.getOwner() != null && item.getOwner().getId().equals(userId)) {
            setLastAndNextBookingToDto(itemOutputDto, itemId);
        } else {
            itemOutputDto.setLastBooking(null);
            itemOutputDto.setNextBooking(null);
        }

        return itemOutputDto;
    }

    @Override
    @Cacheable(value = "items_list", key = "#userId + #pageable.pageNumber + #pageable.pageSize")
    public List<ItemOutputDto> getAllByOwner(Long userId, Pageable pageable) {
        return itemRepositoryJpa.findAllByOwnerId(userId, pageable)
                .stream()
                .map(itemMapper::toOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemOutputDto> search(String text, Pageable pageable) {
        return itemRepositoryJpa.search(text, pageable)
                .stream()
                .map(itemMapper::toOutputDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "items", allEntries = true),
            @CacheEvict(value = "items_list", allEntries = true)
    })
    public Map<String, Boolean> delete(Long itemId) {
        Item item = itemRepositoryJpa.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        itemRepositoryJpa.delete(item);
        return Map.of("deleted", true);
    }

    private User checkUserIsInTable(Long userId) {
        return userRepositoryJpa.findById(userId)
                .orElseThrow(() -> new IllegalOwnerException("no user found"));
    }

    private Item checkItemIsInTable(Long itemId) {
        return itemRepositoryJpa.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
    }

    private Booking checkBookingIsInTable(Long bookingId) {
        return bookingRepositoryJpa.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("no booking found by id"));
    }

    private void validateIsUserOwnerOfItem(Item item, Long userId) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new IllegalOwnerException("Only owner can update item");
        }
    }

    private void updateItemFromDto(ItemInputDto dto, Item item) {
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
    }

    private void checkPastApprovedBooking(Long userId, Long itemId) {
        boolean hasPastApprovedBooking = bookingRepositoryJpa
                .findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .isPresent();

        if (!hasPastApprovedBooking) {
            throw new IllegalArgumentException("User cannot comment without past approved booking");
        }
    }

    private void addParametersToComment(Comment comment, CommentInputDto commentInputDto, Item item, User user) {
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
    }

    private void setCommentToDto(ItemOutputDto dto, Long itemId) {
        dto.setComments(
                commentRepositoryJpa.findAllByItemId(itemId)
                        .stream()
                        .map(commentMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    private void setLastAndNextBookingToDto(ItemOutputDto dto, Long itemId) {
        bookingRepositoryJpa.findLastBooking(
                        LocalDateTime.now(), itemId, PageRequest.of(0, 1))
                .stream()
                .map(bookingMapper::toDto)
                .findFirst()
                .ifPresent(dto::setLastBooking);

        bookingRepositoryJpa.findNextBooking(
                        LocalDateTime.now(), itemId, PageRequest.of(0, 1))
                .stream()
                .map(bookingMapper::toDto)
                .findFirst()
                .ifPresent(dto::setNextBooking);
    }
}
