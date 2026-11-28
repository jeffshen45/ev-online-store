package com.evstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evstore.dto.AdminReportResponse;
import com.evstore.service.AdminReportService;

@RestController
@RequestMapping("/api/admin/reports")

public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    // GET endpoint returning sales totals, revenue, and inventory analytics
    @GetMapping("/sales")
    public ResponseEntity<AdminReportResponse> getSalesReport() {
        AdminReportResponse report = adminReportService.generateSalesReport();
        
        return ResponseEntity.ok(report);
        
    }
}

