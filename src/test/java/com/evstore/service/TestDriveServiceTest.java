package com.evstore.service;

import com.evstore.dto.TestDriveRequest;
import com.evstore.model.TestDriveBooking;
import com.evstore.repository.TestDriveRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class TestDriveServiceTest {

    @Mock
    private TestDriveRepository testDriveRepo;

    @InjectMocks
    private TestDriveService testDriveService;

    private Long testCustomerId;
    private TestDriveRequest validDealershipTestDriveRequest;
    private TestDriveRequest validHomeTestDriveRequest;
    private LocalDate futureBookingDate;
    private TestDriveBooking savedDealershipTestDriveBooking;


    @BeforeEach
    void setUpTestData() {
        testCustomerId = 101L;
        futureBookingDate = LocalDate.now().plusDays(4);

        // standard dealership request
        validDealershipTestDriveRequest = new TestDriveRequest();
        validDealershipTestDriveRequest.setCarModel("CyberSedan Alpha");
        validDealershipTestDriveRequest.setBookingDate(futureBookingDate.toString());
        validDealershipTestDriveRequest.setTimeSlot("14:00");
        validDealershipTestDriveRequest.setTestDriveType("DEALERSHIP");

        // home delivery request
        validHomeTestDriveRequest = new TestDriveRequest();
        validHomeTestDriveRequest.setCarModel("CyberSedan Alpha");
        validHomeTestDriveRequest.setBookingDate(futureBookingDate.toString());
        validHomeTestDriveRequest.setTimeSlot("16:00");
        validHomeTestDriveRequest.setTestDriveType("HOME");
        validHomeTestDriveRequest.setDeliveryAddress("3359 Mississauga Road, Mississauga, ON");

        savedDealershipTestDriveBooking = new TestDriveBooking(testCustomerId, "CyberSedan Alpha", futureBookingDate, "14:00", "DEALERSHIP", null, "CONFIRMED");
    }



    @Test
    @DisplayName("Should successfully schedule a dealership test drive booking when slot is available")
    void shouldScheduleDealershipBookingSuccessfully() {
        when(testDriveRepo.existsByCarModelAndBookingDateAndTimeSlotAndStatus("CyberSedan Alpha", futureBookingDate, "14:00", "CONFIRMED")).thenReturn(false);
        when(testDriveRepo.save(any(TestDriveBooking.class))).thenReturn(savedDealershipTestDriveBooking);

        TestDriveBooking scheduledBookingResult = testDriveService.scheduleBooking(testCustomerId, validDealershipTestDriveRequest);

        assertNotNull(scheduledBookingResult);
        assertEquals("CONFIRMED", scheduledBookingResult.getStatus());
        assertEquals("CyberSedan Alpha", scheduledBookingResult.getCarModel());
        assertEquals("DEALERSHIP", scheduledBookingResult.getTestDriveType());
        verify(testDriveRepo, times(1)).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should successfully schedule an at-home test drive booking with delivery address")
    void shouldScheduleHomeBookingSuccessfully() {
        TestDriveBooking savedHomeBooking = new TestDriveBooking(testCustomerId, "CyberSedan Alpha", futureBookingDate, "16:00", "HOME", "3359 Mississauga Road, Mississauga, ON", "CONFIRMED");

        when(testDriveRepo.existsByCarModelAndBookingDateAndTimeSlotAndStatus("CyberSedan Alpha", futureBookingDate, "16:00", "CONFIRMED")).thenReturn(false);
        when(testDriveRepo.save(any(TestDriveBooking.class))).thenReturn(savedHomeBooking);

        TestDriveBooking scheduledBookingResult = testDriveService.scheduleBooking(testCustomerId, validHomeTestDriveRequest);

        assertNotNull(scheduledBookingResult);
        assertEquals("CONFIRMED", scheduledBookingResult.getStatus());
        assertEquals("HOME", scheduledBookingResult.getTestDriveType());
        assertEquals("3359 Mississauga Road, Mississauga, ON", scheduledBookingResult.getDeliveryAddress());
        verify(testDriveRepo, times(1)).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should throw exception when required fields are missing or empty")
    void shouldFailBookingWhenRequiredFieldsAreEmpty() {
        TestDriveRequest emptyTestDriveRequest = new TestDriveRequest();
        emptyTestDriveRequest.setCarModel("");
        emptyTestDriveRequest.setBookingDate("");
        emptyTestDriveRequest.setTimeSlot("");
        emptyTestDriveRequest.setTestDriveType("");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, emptyTestDriveRequest));

        assertEquals("Car model, booking date, time slot, and test drive type are required.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }

    @Test
    @DisplayName("Should throw exception when booking date string has an invalid date format")
    void shouldFailBookingWhenDateFormatIsInvalid() {
        validDealershipTestDriveRequest.setBookingDate("11-02-2026");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, validDealershipTestDriveRequest));

        assertEquals("Invalid date format. Expected YYYY-MM-DD.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should throw exception when booking date is in the past")
    void shouldFailBookingWhenDateIsInThePast() {
        LocalDate pastBookingDate = LocalDate.now().minusDays(1);
        validDealershipTestDriveRequest.setBookingDate(pastBookingDate.toString());

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, validDealershipTestDriveRequest));

        
        assertEquals("Booking date must be today or a future date.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }

    @Test
    @DisplayName("Should throw exception when test drive type is neither DEALERSHIP nor HOME")
    void shouldFailBookingWhenTestDriveTypeIsInvalid() {
        validDealershipTestDriveRequest.setTestDriveType("TRACK");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, validDealershipTestDriveRequest));

        
        assertEquals("Test drive type must be either DEALERSHIP or HOME.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should throw exception when HOME test drive is selected without a delivery address")
    void shouldFailHomeBookingWhenDeliveryAddressIsMissing() {
        validHomeTestDriveRequest.setDeliveryAddress("");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, validHomeTestDriveRequest));

        assertEquals("Delivery address is required for at-home test drives.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should throw exception when the selected car is already booked for that date and time")
    void shouldFailBookingWhenTimeSlotIsAlreadyReserved() {
        // slot collision check
        when(testDriveRepo.existsByCarModelAndBookingDateAndTimeSlotAndStatus("CyberSedan Alpha", futureBookingDate, "14:00", "CONFIRMED")).thenReturn(true);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> testDriveService.scheduleBooking(testCustomerId, validDealershipTestDriveRequest));

        
        assertEquals("This car is already booked for the selected date and time slot.", thrownException.getMessage());
        verify(testDriveRepo, never()).save(any(TestDriveBooking.class));
    }


    @Test
    @DisplayName("Should retrieve list of bookings belonging to the given customer ID")
    void shouldReturnCustomerBookingsListSuccessfully() {
        when(testDriveRepo.findByCustomerId(testCustomerId)).thenReturn(Collections.singletonList(savedDealershipTestDriveBooking));

        List<TestDriveBooking> customerBookingsListResult = testDriveService.getCustomerBookings(testCustomerId);

        assertNotNull(customerBookingsListResult);
        assertEquals(1, customerBookingsListResult.size());
        assertEquals("CyberSedan Alpha", customerBookingsListResult.get(0).getCarModel());
        verify(testDriveRepo, times(1)).findByCustomerId(testCustomerId);
    }

}

