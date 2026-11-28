package com.evstore.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.evstore.dto.MaintenanceReminderResponse;
import com.evstore.model.PurchaseRecord;
import com.evstore.repository.PurchaseRecordRepository;

@Service
public class MaintenanceReminderService {

    private final PurchaseRecordRepository purchaseRepo;

    public MaintenanceReminderService(PurchaseRecordRepository purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    // scans purchased vehicles for a customer and calculates maintenance reminders based on mileage
    public List<MaintenanceReminderResponse> getRemindersForCustomer(Long customerId) {
        List<PurchaseRecord> purchases = purchaseRepo.findByCustomer_IdOrderByPurchasedAtDesc(customerId);
        List<MaintenanceReminderResponse> reminders = new ArrayList<>();

        for (PurchaseRecord record : purchases) {
            String carName = record.getMake() + " " + record.getModel();
            int mileage = record.getMileage();

            // 1. Tyre rotation & multi-point inspection reminder (every 10,000 km)
            if (mileage >= 10000) {
                String urgency = "UPCOMING";
                if (mileage >= 12000) {
                    urgency = "DUE_NOW";
                }
                reminders.add(new MaintenanceReminderResponse(carName, mileage, "Tire Rotation & Brake Inspection", urgency, "Recommended every 10,000 km for optimal EV tire wear and brake pad longevity."));
            }

            
            
            // 2. Cabinn air filter replacement reminder (around 25,000 km)
            if (mileage >= 25000) {
                String urgency = "UPCOMING";
                if (mileage >= 30000) {
                    urgency = "DUE_NOW";
                }
                reminders.add(new MaintenanceReminderResponse(carName, mileage, "Cabin Air Filter Replacement", urgency, "Cabin air filter should be inspected or replaced to maintain optimal HVAC air quality."));
            }

            // 3. EV high-voltage battery diagnostic & coolant check (every 50,000 km)
            if (mileage >= 50000) {
                String urgency = "UPCOMING";
                if (mileage >= 60000) {
                    urgency = "DUE_NOW";
                }
                reminders.add(new MaintenanceReminderResponse(carName, mileage, "High-Voltage Battery Diagnostic & Coolant Check", urgency, "Comprehensive battery pack health check and thermal management coolant inspection."));
            }

            // 4. Low mileage vehicle notice
            if (mileage < 10000) {
                reminders.add(new MaintenanceReminderResponse(carName, mileage, "Initial Multi-Point EV Health Check", "UPCOMING", "Vehicle is in great condition. Next routine inspection scheduled at 10,000 km."));
            }
        }

        return reminders;
    }
}


