package com.evstore.dto;

import java.math.BigDecimal;
import java.util.Map;



// DTO carrying calculated sales analytics and inventory stats for admin dashboard

public class AdminReportResponse {

    private long totalVehiclesSold;
    private BigDecimal totalRevenue;
    private long totalVehiclesInStock;
    private Map<String, Long> salesByModel; // e.g. "Model 3" -> 3

    public AdminReportResponse() {
    }

    public AdminReportResponse(long totalVehiclesSold, BigDecimal totalRevenue,
                               long totalVehiclesInStock, Map<String, Long> salesByModel) {
        this.totalVehiclesSold = totalVehiclesSold;
        this.totalRevenue = totalRevenue;
        this.totalVehiclesInStock = totalVehiclesInStock;
        this.salesByModel = salesByModel;
    }

    public long getTotalVehiclesSold() {
        return totalVehiclesSold;
    }
    

    public void setTotalVehiclesSold(long totalVehiclesSold) {
        this.totalVehiclesSold =  totalVehiclesSold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
    	
        this.totalRevenue = totalRevenue;
    }

    public long getTotalVehiclesInStock() {
        return totalVehiclesInStock;
    }
    

    public void setTotalVehiclesInStock(long totalVehiclesInStock) {
    	
        this.totalVehiclesInStock = totalVehiclesInStock;
    }

    
    
    public Map<String, Long> getSalesByModel() {
        return salesByModel;
    }

    public void setSalesByModel(Map<String, Long> salesByModel) {
        this.salesByModel = salesByModel;
    }
}

