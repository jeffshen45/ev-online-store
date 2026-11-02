package com.evstore.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
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

    public Vehicle() {}
    
    public Vehicle(String make, String model, int year, BigDecimal price, int mileage) {
    	this.make = make;
    	this.model = model;
    	this.modelYear = year;
    	this.price = price;
    	this.mileage = mileage;
    }

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
    
    public void setModelYear(int year) { 
    	this.modelYear = year; 
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
    
    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", make=" + make + ", model=" + model
                + ", year=" + modelYear + ", price=" + price + ", mileage=" + mileage + "}";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle other)) return false;
        return id != null && id.equals(other.id);
    }
}
