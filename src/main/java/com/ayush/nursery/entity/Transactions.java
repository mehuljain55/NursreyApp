package com.ayush.nursery.entity;

import com.ayush.nursery.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private String description;
    private double amount;

    private Date date;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    private int invoiceId;

}
