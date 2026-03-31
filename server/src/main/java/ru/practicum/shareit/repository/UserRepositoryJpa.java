package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.User;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
