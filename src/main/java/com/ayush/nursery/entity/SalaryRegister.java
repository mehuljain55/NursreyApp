package com.ayush.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "salary_register")
@Data
public class SalaryRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int txnId;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date date;
    private int amount;
    private int employeeId;


}
