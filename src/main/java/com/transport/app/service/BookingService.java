package com.transport.app.service;

import com.transport.app.dto.BookingRequestDTO;
import com.transport.app.dto.BookingResponseDTO;
import com.transport.app.exception.CustomException;
import com.transport.app.model.Booking;
import com.transport.app.model.Booking.BookingStatus;
import com.transport.app.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer for all booking business logic.
 * Controllers call this; this talks to the repository.
 * Never put DB logic directly in controllers!
 */
@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // ─── CREATE BOOKING 
    
    public BookingResponseDTO createBooking(BookingRequestDTO requestDTO) {
        // Map DTO → Entity
        Booking booking = new Booking();
        booking.setCustomerName(requestDTO.getCustomerName().trim());
        booking.setPhone(requestDTO.getPhone().trim());
        booking.setPickupLocation(requestDTO.getPickupLocation().trim());
        booking.setDropLocation(requestDTO.getDropLocation().trim());
        booking.setWeight(requestDTO.getWeight());
        booking.setGoodsType(requestDTO.getGoodsType());
        booking.setDateTime(requestDTO.getDateTime());
        booking.setNotes(requestDTO.getNotes());
        booking.setStatus(BookingStatus.NEW);

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDTO.fromEntity(saved);
    }

    // ─── GET ALL BOOKINGS 
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(BookingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ─── GET BOOKINGS BY STATUS 
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByStatus(String statusStr) {
        try {
            BookingStatus status = BookingStatus.valueOf(statusStr.toUpperCase());
            return bookingRepository.findByStatusOrderByCreatedAtDesc(status)
                    .stream()
                    .map(BookingResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid status: " + statusStr, HttpStatus.BAD_REQUEST);
        }
    }

    // ─── GET SINGLE BOOKING 
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = findBookingOrThrow(id);
        return BookingResponseDTO.fromEntity(booking);
    }

    // ─── UPDATE STATUS 
    
    public BookingResponseDTO updateBookingStatus(Long id, String newStatusStr) {
        Booking booking = findBookingOrThrow(id);

        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(
                "Invalid status '" + newStatusStr + "'. Valid values: NEW, CONFIRMED, IN_TRANSIT, DONE",
                HttpStatus.BAD_REQUEST
            );
        }

        booking.setStatus(newStatus);
        Booking updated = bookingRepository.save(booking);
        return BookingResponseDTO.fromEntity(updated);
    }

    // ─── DASHBOARD STATS 
    
    @Transactional(readOnly = true)
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", bookingRepository.count());
        stats.put("new", bookingRepository.countByStatus(BookingStatus.NEW));
        stats.put("confirmed", bookingRepository.countByStatus(BookingStatus.CONFIRMED));
        stats.put("inTransit", bookingRepository.countByStatus(BookingStatus.IN_TRANSIT));
        stats.put("done", bookingRepository.countByStatus(BookingStatus.DONE));
        return stats;
    }

    // ─── DELETE BOOKING 
    public void deleteBooking(Long id) {
        Booking booking = findBookingOrThrow(id);
        bookingRepository.delete(booking);
    }

    // ─── INTERNAL HELPER
    private Booking findBookingOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                    "Booking not found with id: " + id,
                    HttpStatus.NOT_FOUND
                ));
    }
}