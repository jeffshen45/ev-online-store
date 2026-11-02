package com.evstore.controller;

import com.evstore.dto.TestDriveRequest;
import com.evstore.model.Customer;
import com.evstore.model.TestDriveBooking;
import com.evstore.service.TestDriveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// handles web requests for booking and checking test drives (PB11 / UC06)
@RestController
@RequestMapping("/api/testdrive")
public class TestDriveController {

    @Autowired
    private TestDriveService testDriveService;

    // UC06--> Schedule Test Drive endpoint
    @PostMapping("/book")
    public ResponseEntity<?> scheduleTestDrive(@RequestBody TestDriveRequest req, HttpSession session) {
        //verify active customer session
        Customer loggedInCustomer = (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");
        if (loggedInCustomer == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "You must be signed in to book a test drive.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            //delegate booking logic to service layer
            TestDriveBooking booking = testDriveService.scheduleBooking(loggedInCustomer.getId(), req);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Test drive scheduled successfully.");
            responseBody.put("bookingId", booking.getId());
            responseBody.put("carModel", booking.getCarModel());
            responseBody.put("bookingDate", booking.getBookingDate().toString());
            responseBody.put("timeSlot", booking.getTimeSlot());
            responseBody.put("testDriveType", booking.getTestDriveType());
            responseBody.put("status", booking.getStatus());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (IllegalArgumentException ex) {
            // handle validation and slot collision errors
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // retrieves all test drives for the active customer
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(HttpSession session) {
        Customer loggedInCustomer = (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");
        if (loggedInCustomer == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "You must be signed in to view bookings.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        List<TestDriveBooking> bookings = testDriveService.getCustomerBookings(loggedInCustomer.getId());
        return ResponseEntity.ok(bookings);
    }
}


