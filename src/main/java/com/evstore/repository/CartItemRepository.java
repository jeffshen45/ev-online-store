package com.evstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.evstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomerId(Long customerId);
    
    void deleteByVehicle_Id(Long vehicleId);
}