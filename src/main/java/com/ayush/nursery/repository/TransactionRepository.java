package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions,Integer> {

    @Query("SELECT t FROM Transactions t WHERE t.customerId = :customerId")
    List<Transactions> findTransactions(@Param("customerId") int customerId);


}
