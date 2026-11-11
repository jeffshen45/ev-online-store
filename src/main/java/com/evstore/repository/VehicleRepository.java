package com.evstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.evstore.model.Vehicle;

public interface VehicleRepository
        extends JpaRepository<Vehicle, Long>,
                JpaSpecificationExecutor<Vehicle> {

}
