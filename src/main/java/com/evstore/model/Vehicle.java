
package com.evstore.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue
    private Long id;

    private String make;
    private String model;
    private int modelYear;
    private BigDecimal price;
    private int mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_condition")
    private Condition condition;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private String colour;
    private Integer horsepower;
    private Integer seatingCapacity;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availability = AvailabilityStatus.AVAILABLE;

    public Vehicle() {}

    //Original constructor for compatibility with existing code
    public Vehicle(String make, String model, int year,
                   BigDecimal price, int mileage) {
        this(make, model, year, price, mileage,
             null, null, null, null, null, null,
             AvailabilityStatus.AVAILABLE);
    }

    //Constructor including all vehicle properties
    public Vehicle(String make, String model, int year,
                   BigDecimal price, int mileage,
                   Condition condition,
                   Transmission transmission,
                   FuelType fuelType,
                   String colour,
                   Integer horsepower,
                   Integer seatingCapacity,
                   AvailabilityStatus availability) {

        this.make = make;
        this.model = model;
        this.modelYear = year;
        this.price = price;
        this.mileage = mileage;
        this.condition = condition;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.colour = colour;
        this.horsepower = horsepower;
        this.seatingCapacity = seatingCapacity;
        this.availability = availability;
    }

    //Getters and setters

    public Long getId() {
        return id;
    }

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

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityStatus availability) {
        this.availability = availability;
    }

    //Enum definitions

    public enum Condition {
        FACTORY_NEW("Factory New"),
        LIGHTLY_USED("Lightly Used"),
        USED("Used");

        private final String label;

        Condition(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum Transmission {
        AUTOMATIC("Automatic"),
        MANUAL("Manual");

        private final String label;

        Transmission(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum FuelType {
        ELECTRIC("Electric"),
        HYBRID("Hybrid");

        private final String label;

        FuelType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum AvailabilityStatus {
        AVAILABLE("Available"),
        RESERVED("Reserved"),
        SOLD("Sold");

        private final String label;

        AvailabilityStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    @Override
    public String toString() {
        return "Vehicle{id=" + id
                + ", make=" + make
                + ", model=" + model
                + ", year=" + modelYear
                + ", price=" + price
                + ", mileage=" + mileage
                + ", condition=" + condition
                + ", transmission=" + transmission
                + ", fuelType=" + fuelType
                + ", colour=" + colour
                + ", horsepower=" + horsepower
                + ", seatingCapacity=" + seatingCapacity
                + ", availability=" + availability
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle other)) return false;
        return id != null && id.equals(other.id);
    }
}
