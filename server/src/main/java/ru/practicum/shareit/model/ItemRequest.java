package ru.practicum.shareit.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "item_requests")
@Table
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "description", length = 2000)
    String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    User requestor;
    @Column(name = "created", updatable = false)
    LocalDateTime created;
    @OneToMany(mappedBy = "request")
    List<Item> requestedItems = new ArrayList<>();
}
