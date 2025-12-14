package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {

    @Query("SELECT o FROM Orders o WHERE o.invoice.invoiceId = :invoiceId")
    List<Orders> findOrdersByInvoiceId(@Param("invoiceId") int invoiceId);

}
