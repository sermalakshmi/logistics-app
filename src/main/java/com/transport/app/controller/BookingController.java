package com.transport.app.controller;

import com.transport.app.dto.BookingRequestDTO;
import com.transport.app.dto.BookingResponseDTO;
import com.transport.app.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for booking operations.
 *
 * PUBLIC  endpoints (no JWT needed):
 *   POST /api/bookings          → Customer submits a booking
 *
 * PROTECTED endpoints (JWT required):
 *   GET  /api/bookings          → Admin views all bookings
 *   GET  /api/bookings/{id}     → Admin views one booking
 *   GET  /api/bookings/stats    → Dashboard summary counts
 *   GET  /api/bookings/filter?status=NEW  → Filter by status
 *   PUT  /api/bookings/{id}/status        → Admin updates status
 *   DELETE /api/bookings/{id}  → Admin deletes a booking
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // ─── POST /api/bookings ──────────────────────────────────────────────────────
    /**
     * PUBLIC — Customer submits booking form.
     * No authentication required.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(
            @Valid @RequestBody BookingRequestDTO requestDTO) {

        BookingResponseDTO booking = bookingService.createBooking(requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Booking submitted successfully! We will contact you shortly.");
        response.put("bookingId", booking.getId());
        response.put("data", booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── GET /api/bookings ───────────────────────────────────────────────────────
    /**
     * PROTECTED — Admin views all bookings.
     * Requires valid JWT token in Authorization header.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", bookings.size());
        response.put("data", bookings);

        return ResponseEntity.ok(response);
    }

    // ─── GET /api/bookings/stats ─────────────────────────────────────────────────
    /*
     PROTECTED — Dashboard statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Long> stats = bookingService.getDashboardStats();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", stats);

        return ResponseEntity.ok(response);
    }

    // ─── GET /api/bookings/filter 
    /*
     PROTECTED — Filter bookings by status.
     Example: GET /api/bookings/filter?status=NEW
     */
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getBookingsByStatus(
            @RequestParam String status) {

        List<BookingResponseDTO> bookings = bookingService.getBookingsByStatus(status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", bookings.size());
        response.put("data", bookings);

        return ResponseEntity.ok(response);
    }

    // ─── GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", booking);

        return ResponseEntity.ok(response);
    }

    // ─── PUT /api/bookings/{id}/status
    /*
     PROTECTED — Admin updates the status of a booking.
     Body: { "status": "CONFIRMED" }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newStatus = body.get("status");
        if (newStatus == null || newStatus.isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Status field is required");
            return ResponseEntity.badRequest().body(error);
        }

        BookingResponseDTO updated = bookingService.updateBookingStatus(id, newStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Status updated to: " + updated.getStatus());
        response.put("data", updated);

        return ResponseEntity.ok(response);
    }

    // ─── DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Booking deleted successfully");

        return ResponseEntity.ok(response);
    }
}