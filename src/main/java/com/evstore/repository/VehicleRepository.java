package com.evstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evstore.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
