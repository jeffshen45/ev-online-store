package com.evstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evstore.dto.VehicleStockUpdateRequest;
import com.evstore.model.Vehicle;
import com.evstore.repository.VehicleRepository;

@Service
public class AdminVehicleService {

    private final VehicleRepository vehicleRepo;

    // constructor injection for vehicle repository
    public AdminVehicleService(VehicleRepository vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    // fetch all cars for the admin inventory table
    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    // find single vehicle by id
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepo.findById(id);
    }

    // add new vehicle into stock
    @Transactional
    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicle.getMake() == null || vehicle.getMake().trim().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be empty");
        }
        
        
        if (vehicle.getModel() == null || vehicle.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
        if (vehicle.getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }
        
        
        // deffault status to available if not set
        if (vehicle.getAvailability() == null) {
            vehicle.setAvailability(Vehicle.AvailabilityStatus.AVAILABLE);
        }

        return vehicleRepo.save(vehicle);
    }

    // update price, mileage or status for stock management
    @Transactional
    public Vehicle updateVehicleStock(Long id, VehicleStockUpdateRequest req) {
        Vehicle car = vehicleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));

        if (req.getPrice() != null) {
            car.setPrice(req.getPrice());
        }
        
        if (req.getMileage() != null) {
            car.setMileage(req.getMileage());
        }
        
        
        if (req.getAvailability() != null && !req.getAvailability().trim().isEmpty()) {
            try {
                car.setAvailability(Vehicle.AvailabilityStatus.valueOf(req.getAvailability().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + req.getAvailability());
            }
        }

        return vehicleRepo.save(car);
    }

    
    
    // remove vehicle from inventory
    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepo.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete, vehicle id not found: " + id);
        }
        
        vehicleRepo.deleteById(id);
    }
}
