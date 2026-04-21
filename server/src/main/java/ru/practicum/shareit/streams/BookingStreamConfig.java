package ru.practicum.shareit.streams;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafkaStreams
@Configuration
public class BookingStreamConfig {
    @Bean
    public KStream<String, String> kStream(StreamsBuilder builder) {
        KStream<String, String> source = builder.stream("bookings-topic",
                Consumed.with(Serdes.String(), Serdes.String()));

        source
                .peek((key, value) -> System.out.println("STREAMS-IN: key=" + key + ", value=" + value))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .count(Materialized.as("user-bookings-count-store"))
                .toStream()
                .to("user-activity-stats", Produced.with(Serdes.String(), Serdes.Long()));
        return source;
    }

    @Bean
    public NewTopic statsTopic() {
        return TopicBuilder.name("user-activity-stats")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
