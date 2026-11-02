package com.evstore.repository;

import com.evstore.model.TestDriveBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// DAO interface to run database operations on test drive bookings
@Repository
public interface TestDriveRepository extends JpaRepository<TestDriveBooking, Long> {

    // fetch all bookings associated with a specific customer
    List<TestDriveBooking> findByCustomerId(Long customerId);

    // check if a car is already booked on a specific date and time slot to prevent conflicts
    boolean existsByCarModelAndBookingDateAndTimeSlotAndStatus(
        String carModel, 
        LocalDate bookingDate, 
        String timeSlot, 
        String status
    );
}
