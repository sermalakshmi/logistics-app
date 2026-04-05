package com.transport.app.repository;

import com.transport.app.model.Booking;
import com.transport.app.model.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByOrderByCreatedAtDesc();

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

    List<Booking> findByCustomerNameContainingIgnoreCaseOrderByCreatedAtDesc(String name);

    List<Booking> findByPhoneContaining(String phone);

    List<Booking> findByDateTimeBetweenOrderByDateTimeAsc(
            LocalDateTime start, LocalDateTime end);

    long countByStatus(BookingStatus status);

    List<Booking> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime start, LocalDateTime end);
}