package com.evstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evstore.dto.MaintenanceReminderResponse;
import com.evstore.service.MaintenanceReminderService;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceReminderService maintenanceService;

    
    public MaintenanceController(MaintenanceReminderService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // fetch all service reminders for a specific customers
    @GetMapping("/reminders/{customerId}")
    public ResponseEntity<List<MaintenanceReminderResponse>> getReminders(@PathVariable Long customerId) {
        
    	List<MaintenanceReminderResponse> reminders = maintenanceService.getRemindersForCustomer(customerId);
        return ResponseEntity.ok(reminders);
    }
}

