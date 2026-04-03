package com.yash.finance.controller;

import com.yash.finance.dto.requestDTOs.FinancialRecordDTO;
import com.yash.finance.dto.requestDTOs.responseDTOs.ResponseDTO;
import com.yash.finance.entities.FinanceRecordsEntity;
import com.yash.finance.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/all")
public class CommonController
{
    @Autowired
    private FinancialRecordService financialRecordService;


    @GetMapping("/records")
    public ResponseEntity<List<FinanceRecordsEntity>> getAllRecords(){
        try {
            return ResponseEntity.ok(financialRecordService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<FinanceRecordsEntity> getById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(financialRecordService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FinanceRecordsEntity());
        }
    }

    @GetMapping("/record/filter/category")
    public ResponseEntity<?> getRecordsByCategory(
            @RequestParam String userEmail,
            @RequestParam String category) {
        try {
            List<FinanceRecordsEntity> records = financialRecordService.getRecordsByCategory(userEmail, category);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching records by category: " + e.getMessage());
        }
    }

    @GetMapping("/record/filter/type")
    public ResponseEntity<?> getRecordsByType(
            @RequestParam String userEmail,
            @RequestParam String type) {
        try {
            List<FinanceRecordsEntity> records = financialRecordService.getRecordsByType(userEmail, type);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching records by type: " + e.getMessage());
        }
    }


    @GetMapping("/record/filter/date")
    public ResponseEntity<?> getRecordsByDateRange(
            @RequestParam String userEmail,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            List<FinanceRecordsEntity> records = financialRecordService.getRecordsByDateRange(userEmail, startDate, endDate);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching records by date range: " + e.getMessage());
        }
    }

}
