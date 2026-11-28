package com.evstore.dto;

// DTO representing an individual scheduled maintenance alert for a customer's EV
public class MaintenanceReminderResponse {

    private String vehicleName;     // e.g. "Tesla Model 3"
    private int currentMileage;
    private String serviceType;     // e.g. "Tire Rotation & Inspection", "Battery Diagnostic & Coolant Check"
    private String urgency;         // "UPCOMING" or "DUE_NOW"
    private String recommendation;  // practical guidance message for the customer

    public MaintenanceReminderResponse() {
    }

    public MaintenanceReminderResponse(String vehicleName, int currentMileage, String serviceType, String urgency, String recommendation) {
        this.vehicleName = vehicleName;
        this.currentMileage = currentMileage;
        this.serviceType = serviceType;
        this.urgency = urgency;
        this.recommendation = recommendation;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}

