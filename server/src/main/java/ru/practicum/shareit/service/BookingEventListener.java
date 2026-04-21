package ru.practicum.shareit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.bookingDto.BookingOutputDto;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {
    private final Set<Long> processedOrders = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "bookings-topic", groupId = "shareit-group")
    public void listen(String message) throws JsonProcessingException {
        BookingOutputDto dto = objectMapper.readValue(message, BookingOutputDto.class);
        if (processedOrders.contains(dto.getId())) {
            log.warn("Событие бронирования ID: {} уже обработано. Пропускаем.", dto.getId());
            return;
        }

        log.info("Получено событие о новом бронировании");
        log.info("ID: {}, Вещь: {}, Кто: {}, Статус: {}",
                dto.getId(),
                dto.getItem().getName(),
                dto.getBooker().getName(),
                dto.getStatus());

        processedOrders.add(dto.getId());
    }

    @KafkaListener(topics = "booking-topic.DLT", groupId = "shareit-group")
    public void listenDlt(String message) {
        System.err.println("КРИТИЧЕСКАЯ ОШИБКА: Сообщение ушло в DLT: " + message);
    }
}
