
package com.evstore.dto;

import java.math.BigDecimal;

import com.evstore.model.Vehicle;

public class VehicleFilter {

    // Text filters
    private String make;
    private String model;
    private String colour;

    // Enum filters
    private Vehicle.Condition condition;
    private Vehicle.Transmission transmission;
    private Vehicle.FuelType fuelType;
    private Vehicle.AvailabilityStatus availability;

    // Year range
    private Integer minYear;
    private Integer maxYear;

    // Price range
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // Mileage range
    private Integer minMileage;
    private Integer maxMileage;

    // Horsepower range
    private Integer minHorsepower;
    private Integer maxHorsepower;

    // Seating capacity range
    private Integer minSeatingCapacity;
    private Integer maxSeatingCapacity;

    // Sorting
    private String sort = "price";
    private String dir = "asc";

    // Getters and setters

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Vehicle.Condition getCondition() {
        return condition;
    }

    public void setCondition(Vehicle.Condition condition) {
        this.condition = condition;
    }

    public Vehicle.Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Vehicle.Transmission transmission) {
        this.transmission = transmission;
    }

    public Vehicle.FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(Vehicle.FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Vehicle.AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(Vehicle.AvailabilityStatus availability) {
        this.availability = availability;
    }

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinMileage() {
        return minMileage;
    }

    public void setMinMileage(Integer minMileage) {
        this.minMileage = minMileage;
    }

    public Integer getMaxMileage() {
        return maxMileage;
    }

    public void setMaxMileage(Integer maxMileage) {
        this.maxMileage = maxMileage;
    }

    public Integer getMinHorsepower() {
        return minHorsepower;
    }

    public void setMinHorsepower(Integer minHorsepower) {
        this.minHorsepower = minHorsepower;
    }

    public Integer getMaxHorsepower() {
        return maxHorsepower;
    }

    public void setMaxHorsepower(Integer maxHorsepower) {
        this.maxHorsepower = maxHorsepower;
    }

    public Integer getMinSeatingCapacity() {
        return minSeatingCapacity;
    }

    public void setMinSeatingCapacity(Integer minSeatingCapacity) {
        this.minSeatingCapacity = minSeatingCapacity;
    }

    public Integer getMaxSeatingCapacity() {
        return maxSeatingCapacity;
    }

    public void setMaxSeatingCapacity(Integer maxSeatingCapacity) {
        this.maxSeatingCapacity = maxSeatingCapacity;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
