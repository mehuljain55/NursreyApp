package com.ayush.nursery.repository;

import com.ayush.nursery.entity.SalaryRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalaryRegisterRepository extends JpaRepository<SalaryRegister,Integer> {

    @Query("SELECT s FROM SalaryRegister s WHERE s.employeeId = :employeeId")
    List<SalaryRegister> findSalaryByEmployeeId(@Param("employeeId") int employeeId);


}
