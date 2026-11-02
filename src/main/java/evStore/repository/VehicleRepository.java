package evStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import evStore.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
