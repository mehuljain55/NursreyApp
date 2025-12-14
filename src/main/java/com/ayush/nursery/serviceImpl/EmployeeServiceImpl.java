package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.entity.Salary;
import com.ayush.nursery.entity.SalaryRegister;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.repository.EmployeeRepository;
import com.ayush.nursery.repository.SalaryRegisterRepository;
import com.ayush.nursery.repository.SalaryRepository;
import com.ayush.nursery.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRegisterRepository salaryRegisterRepository;

    @Autowired
    private SalaryRepository salaryRepository;


    public ApiResponseModal findAllEmployees()
    {
        List<Employee> employeeList=employeeRepository.findAll();

        if(employeeList.size()>0)
        {
            return new ApiResponseModal<>(StatusResponse.SUCCESS,employeeList,"Employees list found");
        }else {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"No employee found");
        }
    }

    public ApiResponseModal createEmployee(Employee employee) {

        boolean isError = false;
        List<String> errorMessageList = new ArrayList<>();

        if (employee.getEmployeeName() == null || employee.getEmployeeName().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("Employee name is empty");
        }

        if (employee.getContactNo() != null && !employee.getContactNo().trim().isEmpty()) {
            if (!employee.getContactNo().matches("^[0-9]{10}$")) {
                isError = true;
                errorMessageList.add("Contact number must be 10 digits");
            }
        }

        if (!isError) {
            Employee saveEmployee=employeeRepository.save(employee);
            return new ApiResponseModal<>(StatusResponse.SUCCESS, saveEmployee.getEmployeeId(), "Employee data created");
        }else {
            return new ApiResponseModal<>(StatusResponse.FAILED, errorMessageList, "Errors in employee data");
        }
    }

    public ApiResponseModal addAdvanceSalary(int employeeId, String description, Integer amount, Date date) {

        if (amount == null || amount <= 0) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Invalid amount");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isEmpty()) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Incorrect employeeId");
        }

        if (date == null) {
            date = new Date();
        }

        try {
            Employee employee = employeeOptional.get();

            SalaryRegister salaryRegister = new SalaryRegister();
            salaryRegister.setEmployeeId(employeeId);
            salaryRegister.setDescription(description);
            salaryRegister.setDate(date);
            salaryRegister.setAmount(amount);

            SalaryRegister savedSalary = salaryRegisterRepository.save(salaryRegister);

            if (savedSalary != null && savedSalary.getTxnId() > 0) {

                int currentAdvance = employee.getAdvanceBalance();
                employee.setAdvanceBalance(currentAdvance + amount);
                employeeRepository.save(employee);
            }
            return new ApiResponseModal<>(StatusResponse.SUCCESS, null, "Advance salary added successfully");

        } catch (Exception e) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Failed to add advance salary");
        }
    }

    public ApiResponseModal updateSalary(int employeeId, String description, Integer amount, Integer deduction, Date startDate, Date endDate) {

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Incorrect employeeId");
        }

        if (amount == null || amount <= 0) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Invalid amount");
        }

        if (deduction == null) {
            deduction = 0;
        }

        if (startDate == null || endDate == null) {
            LocalDate today = LocalDate.now();
            LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            startDate = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        }

        try {
            Employee employee = employeeOptional.get();
            int currentAdvance = employee.getAdvanceBalance();

            if (deduction > currentAdvance) {
                return new ApiResponseModal<>(StatusResponse.FAILED, null, "Deduction exceeds advance balance");
            }

            Salary salary = new Salary();
            salary.setEmployeeId(employeeId);
            salary.setDescription(description);
            salary.setAmount(amount);
            salary.setAdvanceDeduction(deduction);
            salary.setStartDate(startDate);
            salary.setEndDate(endDate);
            Salary savedSalary = salaryRepository.save(salary);

            if (savedSalary != null && savedSalary.getTxnId() > 0 && deduction > 0) {
                employee.setAdvanceBalance(currentAdvance - deduction);
                employeeRepository.save(employee);
            }
            return new ApiResponseModal<>(StatusResponse.SUCCESS, null, "Salary updated successfully");
        } catch (Exception e) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Failed to update salary: " + e.getMessage());
        }
    }

}
