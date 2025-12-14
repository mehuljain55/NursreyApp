package com.ayush.nursery.controller;

import com.ayush.nursery.entity.Employee;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/findAll")
    public ApiResponseModal findAllEmployees()
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
                                             @RequestParam("date")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return employeeService.addAdvanceSalary(employeeId, description, amount, date);
    }

}
