package com.ayush.nursery.repository;

import com.ayush.nursery.entity.CreditHistory;
import com.ayush.nursery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory,Integer> {

    @Query("SELECT c FROM CreditHistory c WHERE c.customerId = :customerId")
    List<CreditHistory> findByCustomerId(@Param("customerId") int customerId);


}
