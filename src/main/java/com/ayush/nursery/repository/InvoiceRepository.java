package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId")
    List<Invoice> findInvoices(@Param("customerId") int customerId);

    @Query("SELECT MAX(i.invoiceId) FROM Invoice i")
    Integer findMaxInvoiceId();

    @Query("SELECT i.customer FROM Invoice i WHERE i.invoiceId = :invoiceId")
    Customer findCustomerByInvoiceId(@Param("invoiceId") int invoiceId);

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId AND i.dueAmount > 0 ORDER BY i.date ASC")
    List<Invoice> findDueInvoicesByCustomerId(@Param("customerId") int customerId);

    @Query("SELECT i FROM Invoice i WHERE i.date = :date")
    List<Invoice> findByInvoiceDate(@Param("date") Date date);

    @Query("SELECT COALESCE(SUM(i.finalAmount), 0) FROM Invoice i WHERE i.date BETWEEN :startDate AND :endDate")
    double sumFinalAmountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
