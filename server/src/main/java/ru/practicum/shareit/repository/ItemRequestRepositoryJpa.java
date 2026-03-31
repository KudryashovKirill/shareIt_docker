package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long requestorId);

    List<ItemRequest> findAllByRequestorIdNot(Long requestorId, Pageable pageable);

}
