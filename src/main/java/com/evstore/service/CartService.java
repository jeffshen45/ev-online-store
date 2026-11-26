package com.evstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.evstore.model.CartItem;
import com.evstore.model.Vehicle;
import com.evstore.model.VehicleOption;
import com.evstore.repository.CartItemRepository;
import com.evstore.repository.VehicleOptionRepository;
import com.evstore.repository.VehicleRepository;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final VehicleRepository vehicleRepo;
    private final VehicleOptionRepository optionRepo;

    public CartService(CartItemRepository cartRepo, VehicleRepository vehicleRepo,
                       VehicleOptionRepository optionRepo) {
        this.cartRepo = cartRepo;
        this.vehicleRepo = vehicleRepo;
        this.optionRepo = optionRepo;
    }

    public List<VehicleOption> getOptions(Vehicle vehicle) {
        return optionRepo.findByVehicle(vehicle);
    }

    public CartItem addToCart(Long customerId, Long vehicleId, Map<String, String> choices) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));

        BigDecimal price = vehicle.getPrice();
        for (Map.Entry<String, String> c : choices.entrySet()) {
            VehicleOption option = optionRepo
                    .findByVehicleAndCategoryAndName(vehicle, c.getKey(), c.getValue())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Invalid option: " + c.getKey() + " = " + c.getValue()));
            price = price.add(option.getPriceDelta());
        }
        return cartRepo.save(new CartItem(customerId, vehicle, choices, price));
    }

    public List<CartItem> getCart(Long customerId) {
        return cartRepo.findByCustomerId(customerId);
    }

    public BigDecimal cartTotal(Long customerId) {
        return getCart(customerId).stream()
                .map(CartItem::getUnitPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void remove(Long itemId) {
        cartRepo.deleteById(itemId);
    }
}