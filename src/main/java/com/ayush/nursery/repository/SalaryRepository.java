package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {

    @Query("SELECT s FROM Salary s WHERE s.employeeId = :employeeId")
    List<Salary> findByEmployeeId(@Param("employeeId") int employeeId);

}
