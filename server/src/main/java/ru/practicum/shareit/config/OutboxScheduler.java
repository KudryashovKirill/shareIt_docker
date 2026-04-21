package ru.practicum.shareit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.model.OutboxEvent;
import ru.practicum.shareit.repository.OutboxRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OutboxScheduler(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> events = outboxRepository.findAllByOrderByCreatedAtAsc();
        if (events.isEmpty()) {
            return;
        }
        log.info("Найдено {} событий в Outbox для отправки", events.size());
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload())
                        .get(5, TimeUnit.SECONDS);
                outboxRepository.delete(event);
            } catch (Exception e) {
                log.error("Не удалось отправить событие {}. Остановка цикла до следующего запуска. Ошибка: {}",
                        event.getId(), e.getMessage());
                break;
            }
        }
    }
}
