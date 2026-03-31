package ru.practicum.shareit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name", length = 100)

    String name;
    @Column(name = "email", length = 100)
    @NotNull(message = "email must be not null")
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
