package com.evstore.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// maps test drive reservations to database table
@Entity
@Table(name = "test_drive_bookings")
public class TestDriveBooking {

    // unique booking identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // customer who made the reservation
    @Column(nullable = false)
    private Long customerId;

    
    
    // EV vehicle model selected
    @Column(nullable = false)
    private String carModel;

    // scheduled date for the test drive
    @Column(nullable = false)
    private LocalDate bookingDate;

    // selected time window (e.g., "10:00 AM", "02:00 PM")
    @Column(nullable = false)
    private String timeSlot;

    // DEALERSHIP or HOME
    @Column(nullable = false)
    private String testDriveType;

    // destination address if HOME test drive is chosen
    private String deliveryAddress;

    // current status: CONFIRMED, COMPLETED, CANCELLED
    @Column(nullable = false)
    private String status;

    // empty constructor for JPA
    public TestDriveBooking() {}

    public TestDriveBooking(Long customerId, String carModel, LocalDate bookingDate, String timeSlot, String testDriveType, String deliveryAddress, String status) {
        this.customerId = customerId;
        this.carModel = carModel;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        this.testDriveType = testDriveType;
        this.deliveryAddress = deliveryAddress;
        
        this.status = status;
    }

    // getters and setters
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }

    public Long getCustomerId() {
    	return customerId;
    }
    
    public void setCustomerId(Long customerId) { 
    	this.customerId = customerId; 
    }

    public String getCarModel() {
    	return carModel; 
    }
    public void setCarModel(String carModel) { 
    	this.carModel = carModel;
    }
    

    public LocalDate getBookingDate() { 
    	return bookingDate; 
    }
    public void setBookingDate(LocalDate bookingDate) {
    	this.bookingDate = bookingDate; 
    }

    public String getTimeSlot() {
    	return timeSlot; 
    }
    public void setTimeSlot(String timeSlot) { 
    	this.timeSlot = timeSlot; 
    }

    public String getTestDriveType() {
    	return testDriveType; 
    }
    public void setTestDriveType(String testDriveType) {
    	this.testDriveType = testDriveType; 
    }

    public String getDeliveryAddress() {
    	return deliveryAddress; 
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
    	this.deliveryAddress = deliveryAddress; 
    }

    public String getStatus() {
    	return status; 
    }
    
    public void setStatus(String status) {
    	this.status = status; 
    }
}

