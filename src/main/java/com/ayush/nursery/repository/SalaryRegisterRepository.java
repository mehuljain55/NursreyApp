package com.ayush.nursery.repository;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.entity.SalaryRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRegisterRepository extends JpaRepository<SalaryRegister,Integer> {
}
