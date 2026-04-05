package com.transport.app.dto;

import com.transport.app.model.Booking;
import com.transport.app.model.Booking.BookingStatus;

import java.time.LocalDateTime;

public class BookingResponseDTO {

    private Long id;
    private String customerName;
    private String phone;
    private String pickupLocation;
    private String dropLocation;
    private Double weight;
    private String goodsType;
    private LocalDateTime dateTime;
    private BookingStatus status;
    private String statusLabel;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookingResponseDTO fromEntity(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setCustomerName(booking.getCustomerName());
        dto.setPhone(booking.getPhone());
        dto.setPickupLocation(booking.getPickupLocation());
        dto.setDropLocation(booking.getDropLocation());
        dto.setWeight(booking.getWeight());
        dto.setGoodsType(booking.getGoodsType());
        dto.setDateTime(booking.getDateTime());
        dto.setStatus(booking.getStatus());
        dto.setStatusLabel(resolveLabel(booking.getStatus()));
        dto.setNotes(booking.getNotes());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        return dto;
    }

    private static String resolveLabel(BookingStatus status) {
        if (status == null) return "New";
        return switch (status) {
            case NEW        -> "New";
            case CONFIRMED  -> "Confirmed";
            case IN_TRANSIT -> "In Transit";
            case DONE       -> "Completed";
        };
    }

    public BookingResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getGoodsType() { return goodsType; }
    public void setGoodsType(String goodsType) { this.goodsType = goodsType; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}