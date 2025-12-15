package com.ayush.nursery.controller;

import com.ayush.nursery.dto.EmployeeSalaryDto;
import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.entity.Expense;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/findAll")
    public ApiResponseModal<List<Employee>> findAllEmployees()
    {
        return  employeeService.findAllEmployees();
    }

    @PostMapping("/create")
    public ApiResponseModal createEmployee(@RequestPart("employee") Employee employee)
    {
      return  employeeService.createEmployee(employee);
    }


    @PostMapping("/add/advanceSalary")
    public ApiResponseModal addAdvanceSalary(@RequestParam("employeeId") int employeeId,
                                             @RequestParam("description") String description,
                                             @RequestParam("amount") Integer amount,
                                             @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return employeeService.addAdvanceSalary(employeeId, description, amount, date);
    }


    @PostMapping("/update/salary")
    public ApiResponseModal updateSalary(@RequestParam("employeeId") int employeeId,
                                         @RequestParam("description") String description,
                                         @RequestParam("amount") Integer amount,
                                         @RequestParam("deduction") Integer deduction,
                                         @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                         @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return employeeService.updateSalary(employeeId, description, amount, deduction,startDate,endDate);
    }

    @GetMapping("/findEmployeeSalaryView")
    public ApiResponseModal<EmployeeSalaryDto> findAllEmployees(@RequestParam("employeeId") int employeeId)
    {
        return  employeeService.findEmployeeSalaryList(employeeId);
    }

    @PostMapping("/add/expense")
    public ApiResponseModal addExpense(@RequestParam("description") String description,
                                       @RequestParam("amount") Integer amount,
                                       @RequestParam("date") String date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date utilDate = sdf.parse(date);

            return employeeService.createExpense(description, amount, utilDate);

        } catch (ParseException e) {
            return new ApiResponseModal(
                   StatusResponse.FAILED, null, "Invalid date format. Expected yyyy-MM-dd"
            );
        }
    }

    @GetMapping("/view/expense")
    public ApiResponseModal<List<Expense>> viewExpense(@RequestParam("startDate") String startDate,
                                                       @RequestParam("endDate") String endDate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Date startUtilDate = sdf.parse(startDate);
            Date endUtilDate = sdf.parse(endDate);

            return employeeService.findExpenseByDateRange(startUtilDate, endUtilDate);

        } catch (ParseException e) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Invalid date format. Expected yyyy-MM-dd");
        }
    }



}
