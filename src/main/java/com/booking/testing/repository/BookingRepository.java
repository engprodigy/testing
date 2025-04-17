package com.booking.testing.repository;

import com.booking.testing.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, String> {
    List<BookingEntity> findByUserId(String userId);
}
