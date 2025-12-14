package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {
}
