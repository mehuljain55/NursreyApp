package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Integer> {

    @Query("SELECT e FROM Expense e WHERE e.date BETWEEN :startDate AND :endDate")
    List<Expense> findExpensesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
