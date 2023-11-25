package com.booking.api.repository;

import com.booking.api.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BlockDao extends JpaRepository<Block, UUID> {

    @Query("select b " +
            "from Block b " +
            "where b.property.id = :propertyId " +
            "and b.fromDate <= :toDate " +
            "and b.toDate >= :fromDate")
    List<Block> findBlocksByPropertyAndDateRange(UUID propertyId, LocalDate fromDate, LocalDate toDate);
}
