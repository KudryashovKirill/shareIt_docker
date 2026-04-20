package ru.practicum.shareit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.bookingDto.BookingInputDto;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.repository.BookingRepositoryJpa;
import ru.practicum.shareit.repository.ItemRepositoryJpa;
import ru.practicum.shareit.repository.OutboxRepository;
import ru.practicum.shareit.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.BookingMapper;
import ru.practicum.shareit.util.ItemMapper;
import ru.practicum.shareit.util.exception.IllegalItemException;
import ru.practicum.shareit.util.exception.IllegalOwnerException;
import ru.practicum.shareit.util.exception.MyException;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final OutboxRepository outboxRepository;
    private final BookingMapper bookingMapper;
    private final KafkaTemplate<String, BookingOutputDto> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public BookingServiceImpl(BookingRepositoryJpa bookingRepositoryJpa,
                              UserRepositoryJpa userRepositoryJpa,
                              ItemRepositoryJpa itemRepositoryJpa,
                              OutboxRepository outboxRepository,
                              BookingMapper bookingMapper,
                              KafkaTemplate<String, BookingOutputDto> kafkaTemplate,
                              ObjectMapper objectMapper,
                              ItemMapper itemMapper) {
        this.bookingRepositoryJpa = bookingRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
        this.itemRepositoryJpa = itemRepositoryJpa;
        this.outboxRepository = outboxRepository;
        this.bookingMapper = bookingMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.itemMapper = itemMapper;
    }

    @Transactional
    @Override
    @CacheEvict(value = "user_bookings", key = "#userId")
    public BookingOutputDto create(BookingInputDto dto, Long userId) {
        validateBookingDto(dto);

        User user = checkUserIsInTable(userId);
        Item item = checkItemIsInTable(dto);
        isAvailableItem(item);
        checkBookerIsNotOwner(item, userId);

        Booking booking = bookingMapper.toEntity(dto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        bookingRepositoryJpa.save(booking);
        BookingOutputDto outputDto = bookingMapper.toDto(booking);
        try {
            OutboxEvent event = new OutboxEvent();
            event.setTopic("bookings-topic");
            event.setPayload(objectMapper.writeValueAsString(outputDto));
            event.setKey(String.valueOf(userId));
            outboxRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка подготовки данных для Outbox", e);
        }
//        kafkaTemplate.send("bookings-topic", String.valueOf(outputDto.getId()), outputDto);
        return outputDto;
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "bookings", key = "#bookingId"),
            @CacheEvict(value = "user_bookings", allEntries = true)
    })
    public BookingOutputDto approve(Long bookingId, Long userId, Boolean approved) {
        Booking booking = checkBookingIsInTable(bookingId);
        validateUserForApprovingBooking(booking, userId);
        validateBookingStatusForApprove(booking, userId);
        isOverlapping(booking);
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        bookingRepositoryJpa.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Cacheable(value = "bookings", key = "#bookingId")
    public BookingOutputDto getBookingByBooker(Long bookingId, Long userId) {
        Booking booking = bookingRepositoryJpa.findById(bookingId)
                .orElseThrow(() -> new MyException("no booking found by id"));
        validateUserForBooking(booking, userId);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Cacheable(value = "user_bookings", key = "#userId")
    public List<BookingOutputDto> getAllUsersBookings(Long userId) {
        checkUserIsInTable(userId);
        return bookingRepositoryJpa.findAll()
                .stream()
                .filter(booking -> booking.getBooker().getId().equals(userId))
                .map(bookingMapper::toDto)
                .toList();
    }

    private User checkUserIsInTable(Long userId) {
        return userRepositoryJpa.findById(userId)
                .orElseThrow(() -> new IllegalOwnerException("no user found"));
    }

    private Item checkItemIsInTable(BookingInputDto dto) {
        return itemRepositoryJpa.findById(dto.getItemId())
                .orElseThrow(() -> new MyException("no item found by id"));
    }

    private Booking checkBookingIsInTable(Long bookingId) {
        return bookingRepositoryJpa.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("no booking found by id"));
    }

    private void isAvailableItem(Item item) {
        if (!item.getAvailable()) {
            throw new IllegalItemException("item is not available for booking");
        }
    }

    private void validateBookingDto(BookingInputDto dto) {
        if (dto.getStart().isAfter(dto.getEnd()) || dto.getStart().equals(dto.getEnd())) {
            throw new IllegalArgumentException("start time must be before end time");
        }
    }

    private void validateUserForApprovingBooking(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("wrong owner for approving booking");
        }
    }

    private void validateBookingStatusForApprove(Booking booking, Long userId) {
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new IllegalArgumentException("status has to be waiting");
        }
    }

    private void validateUserForBooking(Booking booking, Long userId) {
        if (!booking.getBooker().getId().equals(userId)) {
            throw new IllegalArgumentException("user is not owner of booking");
        }
    }

    private void checkBookerIsNotOwner(Item item, Long userId) {
        if (item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("owner cant book own item");
        }
    }

    private void isOverlapping(Booking booking) {
        List<Booking> overlapping = bookingRepositoryJpa
                .findOverlappingApprovedBookings(booking.getItem().getId(), booking.getStart(),
                        booking.getEnd());

        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Overlapping approved booking exists");
        }
    }
}
