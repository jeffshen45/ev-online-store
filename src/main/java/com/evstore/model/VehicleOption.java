package com.evstore.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class VehicleOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vehicle vehicle;

    private String category;        //example "Colour"
    private String name;            //example for Colour category "Blue"
    private BigDecimal priceDelta;  //added to the base price

    public VehicleOption() {}

    public VehicleOption(Vehicle vehicle, String category, String name, BigDecimal priceDelta) {
        this.vehicle = vehicle;
        this.category = category;
        this.name = name;
        this.priceDelta = priceDelta;
    }

    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public BigDecimal getPriceDelta() { return priceDelta; }
}
