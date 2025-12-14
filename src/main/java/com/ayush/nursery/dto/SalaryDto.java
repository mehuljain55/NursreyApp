package com.ayush.nursery.dto;

import com.ayush.nursery.enums.SalaryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;

@Data
public class SalaryDto {

    public int sno;
    private String description;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;

    private int amount;
    private int deduction;
    private int finalAmount;
    private Date startDate;
    private Date endDate;


    public SalaryDto(int sno, String description, SalaryType salaryType, int amount, int deduction) {
        this.sno = sno;
        this.description = description;
        this.salaryType = salaryType;
        this.amount = amount;
        this.deduction = deduction;
        this.finalAmount=amount-deduction;
    }
}
