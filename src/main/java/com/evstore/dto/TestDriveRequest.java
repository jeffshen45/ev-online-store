package com.evstore.dto;

// data transfer object to capture test drive form submissions
public class TestDriveRequest {

    private String carModel;
    private String bookingDate; // received as "YYYY-MM-DD" string from the HTML date picker
    private String timeSlot;
    private String testDriveType; // DEALERSHIP or HOME
    private String deliveryAddress;

    public TestDriveRequest() {}

    public TestDriveRequest(String carModel, String bookingDate, String timeSlot, String testDriveType, String deliveryAddress) {
        this.carModel = carModel;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        this.testDriveType = testDriveType;
        this.deliveryAddress = deliveryAddress;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getTestDriveType() {
        return testDriveType;
    }

    public void setTestDriveType(String testDriveType) {
        this.testDriveType = testDriveType;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}


