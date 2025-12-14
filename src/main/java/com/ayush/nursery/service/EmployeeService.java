package com.ayush.nursery.service;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.models.ApiResponseModal;

import java.util.Date;

public interface EmployeeService {

    ApiResponseModal findAllEmployees();
    ApiResponseModal createEmployee(Employee employee);
    ApiResponseModal addAdvanceSalary(int employeeId, String description, Integer amount, Date date);
    ApiResponseModal updateSalary(int employeeId, String description, Integer amount, Integer deduction, Date startDate, Date endDate);

}
