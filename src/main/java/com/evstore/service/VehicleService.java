package com.evstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evstore.model.Vehicle;
import com.evstore.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    public List<Vehicle> getVehicles(Sort sort) {
        return repository.findAll(sort);
    }

    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id);
    }

    public Vehicle save(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
