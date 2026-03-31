package ru.practicum.shareit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name", length = 200)
    @NotNull(message = "name of item cant be null")
    String name;
    @Column(name = "description", length = 2000)

    String description;
    @Column(name = "available")

    Boolean available;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    User owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @OneToMany(mappedBy = "item")
    List<Comment> comments;
}
