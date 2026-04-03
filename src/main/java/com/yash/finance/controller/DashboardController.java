package com.yash.finance.controller;


import com.yash.finance.entities.FinanceRecordsEntity;
import com.yash.finance.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private FinancialRecordService service;

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam String email) {
        try {
            Map<String, Long> summary = service.getDashboardSummary(email);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching summary: " + e.getMessage());
        }
    }


    @GetMapping("/category-totals")
    public ResponseEntity<?> getCategoryTotals(@RequestParam String email) {
        try {
            List<Map<String, Object>> totals = service.getCategoryTotals(email);
            return ResponseEntity.ok(totals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching category totals: " + e.getMessage());
        }
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<?> getRecentActivity(@RequestParam String email,
                                               @RequestParam(defaultValue = "5") int n) {
        try {
            List<FinanceRecordsEntity> records = service.getRecentActivity(email, n);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching recent activity: " + e.getMessage());
        }
    }

    @GetMapping("/trends")
    public ResponseEntity<?> getTrends(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            Map<String, List<Map<String, Object>>> trends = service.getTrends(email, startDate, endDate);
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching trends: " + e.getMessage());
        }
    }
}
