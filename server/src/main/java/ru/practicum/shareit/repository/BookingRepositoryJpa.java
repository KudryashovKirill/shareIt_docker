package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {
    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.item.id = :itemId
                AND b.start < :now
                AND b.status = 'APPROVED'
                ORDER BY b.start DESC
            """)
    List<Booking> findLastBooking(@Param("now") LocalDateTime now,
                                  @Param("itemId") Long itemId,
                                  Pageable pageable);

    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.item.id = :itemId
                AND b.start > :now
                AND b.status = 'APPROVED'
                ORDER BY b.start ASC
            """)
    List<Booking> findNextBooking(@Param("now") LocalDateTime now,
                                  @Param("itemId") Long itemId,
                                  Pageable pageable);

    Optional<Booking> findByBookerIdAndItemIdAndEndBefore(
            Long bookerId,
            Long itemId,
            LocalDateTime end
    );

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.start > :start
            AND b.end < :end
            """
    )
    List<Booking> findOverlappingApprovedBookings(@Param(value = "itemId") Long itemId,
                                                  @Param(value = "start") LocalDateTime start,
                                                  @Param(value = "end") LocalDateTime end);
}
