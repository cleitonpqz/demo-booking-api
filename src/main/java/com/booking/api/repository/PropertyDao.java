package com.booking.api.repository;

import com.booking.api.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyDao extends JpaRepository<Property, UUID> {
}
