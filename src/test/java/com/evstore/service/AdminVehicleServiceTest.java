package com.evstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evstore.dto.VehicleStockUpdateRequest;
import com.evstore.model.Vehicle;
import com.evstore.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class AdminVehicleServiceTest {

    // mock the database repo so we dont hit sqlite during unit tests
    @Mock
    private VehicleRepository vehicleRepo;

    private AdminVehicleService adminVehicleService;

    //setupp service instance with mocked repo before every test run
    @BeforeEach
    void setUp() {
        adminVehicleService = new AdminVehicleService(vehicleRepo);
    }

    // test checking that admin can fetch the entire list of cars in inventory
    @Test
    void testGetAllVehicles_ReturnsList() {
        Vehicle car1 = new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 0);
        Vehicle car2 = new Vehicle("Ford", "Mach-E", 2024, new BigDecimal("52000"), 12000);

        when(vehicleRepo.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Vehicle> result = adminVehicleService.getAllVehicles();
        assertEquals(2, result.size());
        verify(vehicleRepo, times(1)).findAll();
    }

    // verify fetching a specific car by its id works properly
    @Test
    void testGetVehicleById_Success() {
        Vehicle car = new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 0);
        when(vehicleRepo.findById(1L)).thenReturn(Optional.of(car));

        Optional<Vehicle> found = adminVehicleService.getVehicleById(1L);
        
        assertTrue(found.isPresent());
        assertEquals("Model 3", found.get().getModel());
    }

    // test adding a brand new vehicle to stock and verify default status gets set to AVAILABLE
    @Test
    void testAddVehicle_Success() {
        Vehicle car = new Vehicle("Hyundai", "Ioniq 5", 2024, new BigDecimal("42000"), 5000);
        when(vehicleRepo.save(any(Vehicle.class))).thenReturn(car);

        Vehicle saved = adminVehicleService.addVehicle(car);
        assertNotNull(saved);
        assertEquals("Ioniq 5", saved.getModel());
        assertEquals(Vehicle.AvailabilityStatus.AVAILABLE, saved.getAvailability());
        verify(vehicleRepo, times(1)).save(car);
    }

    // ensure validation catches empty make and throws illegal argument exception
    @Test
    void testAddVehicle_MissingMake_ThrowsException() {
        Vehicle car = new Vehicle("", "Ioniq 5", 2024, new BigDecimal("42000"), 5000);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> adminVehicleService.addVehicle(car));
        assertEquals("Make cannot be empty", ex.getMessage());
    }

    // test updating stock details like mileage, price, and changing availabiliyty to RESERVED
    @Test
    void testUpdateVehicleStock_Success() {
        Vehicle car = new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 100);
        when(vehicleRepo.findById(1L)).thenReturn(Optional.of(car));
        when(vehicleRepo.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));
        VehicleStockUpdateRequest req = new VehicleStockUpdateRequest(500, new BigDecimal("43000"), "RESERVED");
        Vehicle updated = adminVehicleService.updateVehicleStock(1L, req);

        assertEquals(500, updated.getMileage());
        assertEquals(new BigDecimal("43000"), updated.getPrice());
        assertEquals(Vehicle.AvailabilityStatus.RESERVED, updated.getAvailability());
    }

    // test that passing a bogus status string throws validation error
    @Test
    void testUpdateVehicleStock_InvalidStatus_ThrowsException() {
        Vehicle car = new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 100);
        when(vehicleRepo.findById(1L)).thenReturn(Optional.of(car));

        VehicleStockUpdateRequest req = new VehicleStockUpdateRequest(null, null, "NOT_REAL_STATUS");
        assertThrows(IllegalArgumentException.class, () -> adminVehicleService.updateVehicleStock(1L, req));
    }

    //test deleting an existing vehicle from inventory
    @Test
    void testDeleteVehicle_Success() {
        when(vehicleRepo.existsById(1L)).thenReturn(true);
        doNothing().when(vehicleRepo).deleteById(1L);
        assertDoesNotThrow(() -> adminVehicleService.deleteVehicle(1L));
        verify(vehicleRepo, times(1)).deleteById(1L);
    }

    
    // verify that attempting to delete a vehicle id that doesn't exist throws an exception
    @Test
    void testDeleteVehicle_NotFound_ThrowsException() {
        when(vehicleRepo.existsById(99L)).thenReturn(false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> adminVehicleService.deleteVehicle(99L));
        assertTrue(ex.getMessage().contains("Cannot delete"));
    }
}


