package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.bankAccountNo = :bankAccountNo")
    boolean existsAccount(@Param("bankAccountNo") String bankAccountNo);

    @Query(value = """
       SELECT CASE 
                WHEN COUNT(*) > 0 THEN true 
                ELSE false 
              END
       FROM customer_detail
       WHERE customer_id = :customerId
       """, nativeQuery = true)
    boolean existsCustomerById(@Param("customerId") int customerId);



}
