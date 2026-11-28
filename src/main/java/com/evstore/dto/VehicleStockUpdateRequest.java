package com.evstore.dto;

import java.math.BigDecimal;

// DTO for admin updating vehicle stock, pricing or status
public class VehicleStockUpdateRequest {
    private Integer mileage;
    private BigDecimal price;
    private String availability; // AVAILABLE, RESERVED, or SOLD

    public VehicleStockUpdateRequest() {
    }

    public VehicleStockUpdateRequest(Integer mileage, BigDecimal price, String availability) {
        this.mileage = mileage;
        this.price = price;
        this.availability = availability;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}


