package com.evstore.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.evstore.dto.AdminReportResponse;
import com.evstore.model.PurchaseRecord;
import com.evstore.model.Vehicle;
import com.evstore.repository.PurchaseRecordRepository;
import com.evstore.repository.VehicleRepository;

@Service
public class AdminReportService {

    private final PurchaseRecordRepository purchaseRepo;
    private final VehicleRepository vehicleRepo;

    // inject existingg purchase and vehicle repositories
    public AdminReportService(PurchaseRecordRepository purchaseRepo, VehicleRepository vehicleRepo) {
        this.purchaseRepo = purchaseRepo;
        this.vehicleRepo = vehicleRepo;
    }

    // calculates dealership sales summaries and current inventory totals
    
    public AdminReportResponse generateSalesReport() {
        List<PurchaseRecord> purchases = purchaseRepo.findAll();
        List<Vehicle> vehicles = vehicleRepo.findAll();

        long totalSold = purchases.size();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        
        Map<String, Long> salesByModel = new HashMap<>();

        for (PurchaseRecord record : purchases) {
            // sum up total revenue from final sale price
            if (record.getFinalPrice() != null) {
                totalRevenue = totalRevenue.add(record.getFinalPrice());
            }

            
            // count sales per EV model
            String model = record.getModel();
            if (model != null && !model.isBlank()) {
                salesByModel.put(model, salesByModel.getOrDefault(model, 0L) + 1L);
            }
            
        }

        // countt how many cars are currently available in inventory
        long inStock = vehicles.stream()
                .filter(v -> v.getAvailability() == Vehicle.AvailabilityStatus.AVAILABLE)
                .count();

        
        
        return new AdminReportResponse(totalSold, totalRevenue, inStock, salesByModel);
    }
}

