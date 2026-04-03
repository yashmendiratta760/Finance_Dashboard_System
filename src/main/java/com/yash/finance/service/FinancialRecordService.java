package com.yash.finance.service;


import com.yash.finance.dto.requestDTOs.FinancialRecordDTO;
import com.yash.finance.dto.requestDTOs.UpdateFinancialDTO;
import com.yash.finance.entities.FinanceRecordsEntity;
import com.yash.finance.entities.UserEntity;
import com.yash.finance.repository.FinancialRepo;
import com.yash.finance.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinancialRecordService
{
    @Autowired
    private FinancialRepo financialRepo;

    @Autowired
    private UserRepo userRepo;

    public FinanceRecordsEntity createEntry(FinancialRecordDTO financialRecordDTO) throws Exception{
        FinanceRecordsEntity financeRecordsEntity = new FinanceRecordsEntity();
        boolean userExists = userRepo.findById(financialRecordDTO.getUserId()).isPresent();
        UserEntity user;
        if (userExists){
            user = userRepo.findById(financialRecordDTO.getUserId()).orElseGet(UserEntity::new);
        }
        else{
            throw new Exception("user doesn't exist");
        }

        financeRecordsEntity.setAmount(financialRecordDTO.getAmount());
        financeRecordsEntity.setDate(financialRecordDTO.getDate());
        financeRecordsEntity.setCategory(financialRecordDTO.getCategory());
        financeRecordsEntity.setType(financialRecordDTO.getType());
        financeRecordsEntity.setDescription(financialRecordDTO.getDescription());
        financeRecordsEntity.setUser(user);
        financialRepo.save(financeRecordsEntity);

        return financeRecordsEntity;
    }

    public FinanceRecordsEntity updateEntry(UpdateFinancialDTO updateDTO) throws Exception {
        FinanceRecordsEntity record = financialRepo.findById(updateDTO.getFId())
                .orElseThrow(() -> new Exception("Financial record not found"));

        if (updateDTO.getAmount() != null) record.setAmount(updateDTO.getAmount());
        if (updateDTO.getDate() != null) record.setDate(updateDTO.getDate());
        if (updateDTO.getCategory() != null) record.setCategory(updateDTO.getCategory());
        if (updateDTO.getType() != null) record.setType(updateDTO.getType());
        if (updateDTO.getDescription() != null) record.setDescription(updateDTO.getDescription());

        // Optional: update user if provided
        if (updateDTO.getUserId() != null) {
            UserEntity user = userRepo.findById(updateDTO.getUserId())
                    .orElseThrow(() -> new Exception("User doesn't exist"));
            record.setUser(user);
        }

        return financialRepo.save(record);
    }

    public void deleteEntry(Long id){
        financialRepo.deleteById(id);
    }

    public List<FinanceRecordsEntity> getAll(){
        return financialRepo.findAll();
    }
    public FinanceRecordsEntity getById(Long id){
        return financialRepo.findById(id).orElseGet(FinanceRecordsEntity::new);
    }

    public List<FinanceRecordsEntity> getRecordsByCategory(String userEmail, String category) {
        if (category == null || category.isEmpty()) {
            return new ArrayList<>(); // return empty list if no category provided
        }
        return financialRepo.findByUser_EmailAndCategory(userEmail, category);
    }



    public List<FinanceRecordsEntity> getRecordsByType(String userEmail, String type) {
        if (type == null || type.isEmpty()) {
            return new ArrayList<>();
        }
        return financialRepo.findByUser_EmailAndType(userEmail, type);
    }


    public List<FinanceRecordsEntity> getRecordsByDateRange(String userEmail, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        return financialRepo.findByUser_EmailAndDateBetween(userEmail, startDate, endDate);
    }

    public Map<String, Long> getDashboardSummary(String email) {
        Map<String, Long> summary = new HashMap<>();
        Long income = Optional.ofNullable(financialRepo.getTotalIncome(email)).orElse(0L);
        Long expense = Optional.ofNullable(financialRepo.getTotalExpense(email)).orElse(0L);
        summary.put("totalIncome", income);
        summary.put("totalExpense", expense);
        summary.put("netBalance", income - expense);
        return summary;
    }

    public List<Map<String, Object>> getCategoryTotals(String email) {
        List<Object[]> results = financialRepo.getTotalsByCategory(email);
        return results.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("category", r[0]);
            map.put("total", r[1]);
            return map;
        }).collect(Collectors.toList());
    }


    public List<FinanceRecordsEntity> getRecentActivity(String email, int n) {

            Pageable pageable = PageRequest.of(0, n, Sort.by("date").descending());
            return financialRepo.findByUser_Email(email, pageable);

    }


    public Map<String, List<Map<String, Object>>> getTrends(String email, Date startDate, Date endDate) {
        Map<String, List<Map<String, Object>>> trends = new HashMap<>();

        for (String type : Arrays.asList("Credit", "Debit")) {
            List<Object[]> data = financialRepo.getTrends(email, type, startDate, endDate);
            List<Map<String, Object>> list = data.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", r[0]);
                map.put("amount", r[1]);
                return map;
            }).collect(Collectors.toList());
            trends.put(type.toLowerCase(), list);
        }

        return trends;
    }


}
