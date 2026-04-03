package com.yash.finance.repository;

import com.yash.finance.entities.FinanceRecordsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface FinancialRepo extends JpaRepository<FinanceRecordsEntity,Long> {

    List<FinanceRecordsEntity> findByUser_EmailAndCategory(String userEmail, String category);

    List<FinanceRecordsEntity> findByUser_EmailAndType(String userEmail, String type);

    List<FinanceRecordsEntity> findByUser_EmailAndDateBetween(String userEmail, Date startDate, Date endDate);

    @Query("SELECT SUM(f.amount) FROM FinanceRecordsEntity f WHERE f.user.email = :email AND f.type = 'Credit'")
    Long getTotalIncome(String email);

    @Query("SELECT SUM(f.amount) FROM FinanceRecordsEntity f WHERE f.user.email = :email AND f.type = 'Debit'")
    Long getTotalExpense(String email);

    @Query("SELECT f.category as category, SUM(f.amount) as total FROM FinanceRecordsEntity f " +
            "WHERE f.user.email = :email GROUP BY f.category")
    List<Object[]> getTotalsByCategory(String email);

    List<FinanceRecordsEntity> findByUser_Email(String email, Pageable pageable);

    @Query("SELECT f.date, SUM(f.amount) FROM FinanceRecordsEntity f " +
            "WHERE f.user.email = :email AND f.type = :type AND f.date BETWEEN :startDate AND :endDate " +
            "GROUP BY f.date ORDER BY f.date")
    List<Object[]> getTrends(String email, String type, Date startDate, Date endDate);
}


