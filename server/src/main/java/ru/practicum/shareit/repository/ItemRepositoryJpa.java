package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.Item;

import java.util.List;

@Repository
public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = TRUE " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);
}
