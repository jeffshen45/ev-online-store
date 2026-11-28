package com.evstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evstore.dto.MaintenanceReminderResponse;
import com.evstore.model.PurchaseRecord;
import com.evstore.repository.PurchaseRecordRepository;

@ExtendWith(MockitoExtension.class)
public class MaintenanceReminderServiceTest {

    @Mock
    private PurchaseRecordRepository purchaseRepo;

    private MaintenanceReminderService maintenanceService;

    @BeforeEach
    void setUp() {
        maintenanceService = new MaintenanceReminderService(purchaseRepo);
    }

    @Test
    void testGetReminders_NoPurchases() {
        when(purchaseRepo.findByCustomer_IdOrderByPurchasedAtDesc(1L)).thenReturn(Collections.emptyList());
        List<MaintenanceReminderResponse> result = maintenanceService.getRemindersForCustomer(1L);
        
        assertTrue(result.isEmpty());
    }

    
    
    @Test
    void testGetReminders_LowMileageVehicle() {
        PurchaseRecord record = new PurchaseRecord();
        record.setMake("Tesla");
        record.setModel("Model 3");
        record.setMileage(4000);

        when(purchaseRepo.findByCustomer_IdOrderByPurchasedAtDesc(1L)).thenReturn(List.of(record));

        List<MaintenanceReminderResponse> result = maintenanceService.getRemindersForCustomer(1L);
        assertEquals(1, result.size());
        
        assertEquals("Initial Multi-Point EV Health Check", result.get(0).getServiceType());
        assertEquals("UPCOMING", result.get(0).getUrgency());
    }

    @Test
    void testGetReminders_HighMileageVehicle() {
        PurchaseRecord record = new PurchaseRecord();
        record.setMake("Hyundai");
        record.setModel("Ioniq 5");
        record.setMileage(55000);

        when(purchaseRepo.findByCustomer_IdOrderByPurchasedAtDesc(1L)).thenReturn(List.of(record));

        List<MaintenanceReminderResponse> result = maintenanceService.getRemindersForCustomer(1L);
        assertEquals(3, result.size());
        
        assertEquals("DUE_NOW", result.get(0).getUrgency());
        
        assertEquals("High-Voltage Battery Diagnostic & Coolant Check", result.get(2).getServiceType());
    }
}

