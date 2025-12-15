package com.ayush.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "expense")
@Data
public class Expense {

    private int expenseId;
    private String description;
    private int amount;

    @Transient
    private String expenseDate;

    @Temporal(TemporalType.DATE)
    private Date date;

}
