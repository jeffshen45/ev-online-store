
package com.evstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.evstore.dto.VehicleFilter;
import com.evstore.model.Vehicle;
import com.evstore.repository.VehicleRepository;

@Service
public class VehicleService {

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("price", "mileage");

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    public List<Vehicle> getVehicles(String sortField, String direction) {
        String field = SORTABLE_FIELDS.contains(sortField)
                ? sortField : "price";

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return repository.findAll(sort);
    }

    // Search vehicles using any combination of filters
    public List<Vehicle> searchVehicles(VehicleFilter filter) {

        Specification<Vehicle> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Text filters
            addTextFilter(predicates, root, cb,
                    "make", filter.getMake());

            addTextFilter(predicates, root, cb,
                    "model", filter.getModel());

            addTextFilter(predicates, root, cb,
                    "colour", filter.getColour());

            // Enum filters
            if (filter.getCondition() != null) {
                predicates.add(
                        cb.equal(root.get("condition"),
                                filter.getCondition()));
            }

            if (filter.getTransmission() != null) {
                predicates.add(
                        cb.equal(root.get("transmission"),
                                filter.getTransmission()));
            }

            if (filter.getFuelType() != null) {
                predicates.add(
                        cb.equal(root.get("fuelType"),
                                filter.getFuelType()));
            }

            if (filter.getAvailability() != null) {
                predicates.add(
                        cb.equal(root.get("availability"),
                                filter.getAvailability()));
            }

            // Year range
            if (filter.getMinYear() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.<Integer>get("modelYear"),
                                filter.getMinYear()));
            }

            if (filter.getMaxYear() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.<Integer>get("modelYear"),
                                filter.getMaxYear()));
            }

            // Price range
            if (filter.getMinPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.<BigDecimal>get("price"),
                                filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.<BigDecimal>get("price"),
                                filter.getMaxPrice()));
            }

            // Mileage range
            if (filter.getMinMileage() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.<Integer>get("mileage"),
                                filter.getMinMileage()));
            }

            if (filter.getMaxMileage() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.<Integer>get("mileage"),
                                filter.getMaxMileage()));
            }

            // Horsepower range
            if (filter.getMinHorsepower() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.<Integer>get("horsepower"),
                                filter.getMinHorsepower()));
            }

            if (filter.getMaxHorsepower() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.<Integer>get("horsepower"),
                                filter.getMaxHorsepower()));
            }

            // Seating capacity range
            if (filter.getMinSeatingCapacity() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.<Integer>get("seatingCapacity"),
                                filter.getMinSeatingCapacity()));
            }

            if (filter.getMaxSeatingCapacity() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.<Integer>get("seatingCapacity"),
                                filter.getMaxSeatingCapacity()));
            }

            // Combine all selected filters using AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Preserve sorting functionality
        String requestedSort = filter.getSort();

        String field = requestedSort != null
                && SORTABLE_FIELDS.contains(requestedSort)
                ? requestedSort : "price";

        Sort sort = "desc".equalsIgnoreCase(filter.getDir())
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return repository.findAll(specification, sort);
    }

    // Case-insensitive partial text matching
    private void addTextFilter(
            List<Predicate> predicates,
            Root<Vehicle> root,
            CriteriaBuilder cb,
            String field,
            String value) {

        if (value != null && !value.isBlank()) {

            String searchValue =
                    "%" + value.trim().toLowerCase() + "%";

            predicates.add(
                    cb.like(
                            cb.lower(root.<String>get(field)),
                            searchValue));
        }
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
