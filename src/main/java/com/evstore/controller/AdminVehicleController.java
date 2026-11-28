package com.evstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evstore.dto.VehicleStockUpdateRequest;
import com.evstore.model.Vehicle;
import com.evstore.service.AdminVehicleService;

@RestController
@RequestMapping("/api/admin/vehicles")
public class AdminVehicleController {

    private final AdminVehicleService adminVehicleService;

    public AdminVehicleController(AdminVehicleService adminVehicleService) {
        this.adminVehicleService = adminVehicleService;
    }

    //vieww full inventory list
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(adminVehicleService.getAllVehicles());
    }

    //get single vehicle details
    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id) {
        return adminVehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    
    // addd new vehicle into store catalog
    @PostMapping
    public ResponseEntity<?> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle created = adminVehicleService.addVehicle(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } 
        catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // update pricing, mileage, or stock availability
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody VehicleStockUpdateRequest req) {
        try {
            Vehicle updated = adminVehicleService.updateVehicleStock(id, req);
            return ResponseEntity.ok(updated);
        } 
        catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // delete vehiclee  from catalog
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        try {
            adminVehicleService.deleteVehicle(id);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Vehicle " + id + " successfully deleted");
            return ResponseEntity.ok(res);
        } 
        catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}

