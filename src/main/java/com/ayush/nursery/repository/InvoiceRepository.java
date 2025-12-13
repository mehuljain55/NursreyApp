package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId")
    List<Invoice> findInvoices(@Param("customerId") int customerId);



}
