package com.transport.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
public class Booking {

    // ─── Status Enum ────────────────────────────────────────────────────────────
    public enum BookingStatus {
        NEW,        // Just submitted by customer
        CONFIRMED,  // Admin has confirmed
        IN_TRANSIT, // Goods are being transported
        DONE        // Delivery completed
    }

    // ─── Fields ─────────────────────────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "Phone number is required")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotBlank(message = "Pickup location is required")
    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @NotBlank(message = "Drop location is required")
    @Column(name = "drop_location", nullable = false)
    private String dropLocation;

    @Column(name = "weight")
    private Double weight;  // in KG

    @Column(name = "goods_type")
    private String goodsType;  // e.g., Electronics, Furniture, Food, etc.

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.NEW;

    @Column(name = "notes", length = 500)
    private String notes;  // Optional customer notes

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ─── Lifecycle Hooks ─────────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = BookingStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ─── Constructors ────────────────────────────────────────────────────────────
    public Booking() {}

    public Booking(String customerName, String phone, String pickupLocation,
                   String dropLocation, Double weight, String goodsType,
                   LocalDateTime dateTime) {
        this.customerName = customerName;
        this.phone = phone;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.weight = weight;
        this.goodsType = goodsType;
        this.dateTime = dateTime;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────
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

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", customer='" + customerName +
               "', status=" + status + ", pickup='" + pickupLocation +
               "', drop='" + dropLocation + "'}";
    }
}