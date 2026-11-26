package com.evstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.evstore.model.Vehicle;
import com.evstore.model.VehicleOption;

public interface VehicleOptionRepository extends JpaRepository<VehicleOption, Long> {
    List<VehicleOption> findByVehicle(Vehicle vehicle);
    Optional<VehicleOption> findByVehicleAndCategoryAndName(Vehicle vehicle, String category, String name);
    
    void deleteByVehicle_Id(Long vehicleId);
}
