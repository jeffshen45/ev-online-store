package com.evstore.service;

import com.evstore.dto.TestDriveRequest;
import com.evstore.model.TestDriveBooking;
import com.evstore.repository.TestDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

//service layer for test drive booking operations
@Service
public class TestDriveService {

    @Autowired
    private TestDriveRepository testDriveRepo;

    // schedule and validate a test drive reservation (UC06)
    public TestDriveBooking scheduleBooking(Long customerId, TestDriveRequest request) {

        //check that essential inputs are not empty
        if (request.getCarModel() == null || request.getCarModel().trim().isEmpty() || request.getBookingDate() == null || request.getBookingDate().trim().isEmpty() ||request.getTimeSlot() == null || request.getTimeSlot().trim().isEmpty() ||
            request.getTestDriveType() == null || request.getTestDriveType().trim().isEmpty()) {
            throw new IllegalArgumentException("Car model, booking date, time slot, and test drive type are required.");
        }

        //  parse booking date string into LocalDate
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(request.getBookingDate().trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD.");
        }

        //prevent booking in the past
        if (parsedDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date must be today or a future date.");
        }

        
        
        //normalize test drive type
        String type = request.getTestDriveType().trim().toUpperCase();
        if (!type.equals("DEALERSHIP") && !type.equals("HOME")) {
            throw new IllegalArgumentException("Test drive type must be either DEALERSHIP or HOME.");
        }

        // if HOME test drive is chosen, delivery address is required
        String address = (request.getDeliveryAddress() != null) ? request.getDeliveryAddress().trim() : "";
        if (type.equals("HOME") && address.isEmpty()) {
            throw new IllegalArgumentException("Delivery address is required for at-home test drives.");
        }

        // check if this specific vehicle is already booked for this slot
        boolean slotTaken = testDriveRepo.existsByCarModelAndBookingDateAndTimeSlotAndStatus(
            request.getCarModel().trim(),
            parsedDate,
            request.getTimeSlot().trim(),
            "CONFIRMED"
        );

        if (slotTaken) {
            throw new IllegalArgumentException("This car is already booked for the selected date and time slot.");
        }

        // build booking entity with CONFIRMED status
        TestDriveBooking bookingToSave = new TestDriveBooking(
            customerId,
            request.getCarModel().trim(),
            parsedDate,
            request.getTimeSlot().trim(),
            type,
            type.equals("HOME") ? address : null,
            "CONFIRMED"
        );

        // persist booking to database
        TestDriveBooking savedBooking = testDriveRepo.save(bookingToSave);

        // trigger automated reminder log
        sendBookingReminder(savedBooking);
        return savedBooking;
    }

    // retrieve list of bookings for the currently logged in customer
    public List<TestDriveBooking> getCustomerBookings(Long customerId) {
        return testDriveRepo.findByCustomerId(customerId);
    }

    // automated reminder service simulation for PB11
    private void sendBookingReminder(TestDriveBooking booking) {
        System.out.println("[TEST DRIVE REMINDER] Booking #" + booking.getId() + " confirmed for Car: " + booking.getCarModel() + " on " + booking.getBookingDate() +" at " + booking.getTimeSlot() +" (Type: " + booking.getTestDriveType() + ").");
    }
}

