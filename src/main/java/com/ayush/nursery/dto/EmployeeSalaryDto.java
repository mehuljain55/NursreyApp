package com.ayush.nursery.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeSalaryDto {

    private int totalAmount;
    private int totalDeduction;
    private int finalAmount;
    private List<SalaryDto> salaryList;

}
