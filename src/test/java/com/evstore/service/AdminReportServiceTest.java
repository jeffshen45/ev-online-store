package com.evstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evstore.dto.AdminReportResponse;
import com.evstore.model.PurchaseRecord;
import com.evstore.model.Vehicle;
import com.evstore.repository.PurchaseRecordRepository;
import com.evstore.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class AdminReportServiceTest {

    // mock repositories so we test analytics calculation in memory
    @Mock
    private PurchaseRecordRepository purchaseRepo;

    @Mock
    private VehicleRepository vehicleRepo;

    private AdminReportService adminReportService;

    @BeforeEach
    void setUp() {
        adminReportService =  new AdminReportService(purchaseRepo, vehicleRepo);
    }

    // test empty scenario where no sales or inventory exist yet
    @Test
    void testGenerateSalesReport_EmptyData() {
        when(purchaseRepo.findAll()).thenReturn(Collections.emptyList());
        
        when(vehicleRepo.findAll()).thenReturn(Collections.emptyList());

        AdminReportResponse report = adminReportService.generateSalesReport();

        
        
        assertEquals(0, report.getTotalVehiclesSold());
        
        assertEquals(BigDecimal.ZERO, report.getTotalRevenue());
        assertEquals(0, report.getTotalVehiclesInStock());
        assertTrue(report.getSalesByModel().isEmpty());
    }

    // test sales aggregattion, revenue summation, and model breakdown
    @Test
    void testGenerateSalesReport_CalculatesCorrectMetrics() {
        PurchaseRecord p1 = new PurchaseRecord();
        p1.setModel("Model 3");
        p1.setFinalPrice(new BigDecimal("45000"));

        PurchaseRecord p2 = new PurchaseRecord();
        p2.setModel("Model 3");
        p2.setFinalPrice(new BigDecimal("48000"));

        PurchaseRecord p3 = new PurchaseRecord();
        p3.setModel("Ioniq 5");
        
        p3.setFinalPrice(new BigDecimal("42000"));

        when(purchaseRepo.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Vehicle v1 = new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 0);
        
        v1.setAvailability(Vehicle.AvailabilityStatus.AVAILABLE);

        Vehicle v2 = new Vehicle("Ford", "Mach-E", 2024, new BigDecimal("52000"), 5000);
        v2.setAvailability(Vehicle.AvailabilityStatus.SOLD);

        when(vehicleRepo.findAll()).thenReturn(Arrays.asList(v1, v2));

        AdminReportResponse report = adminReportService.generateSalesReport();

        // 3 vehicles sold
        assertEquals(3, report.getTotalVehiclesSold());
        
        // 45000 + 48000 + 42000 = 135000
        assertEquals(new BigDecimal("135000"), report.getTotalRevenue());
        
        // only 1 vehicle in stock (v1 is AVAILABLE, v2 is SOLD)
        assertEquals(1, report.getTotalVehiclesInStock());
        // modeel breakdown
        assertEquals(2L, report.getSalesByModel().get("Model 3"));
        assertEquals(1L, report.getSalesByModel().get("Ioniq 5"));
    }
}

