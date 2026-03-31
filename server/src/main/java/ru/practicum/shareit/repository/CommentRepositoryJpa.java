package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.Comment;

import java.util.List;

@Repository
public interface CommentRepositoryJpa extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(Long itemId);
}
