package com.ayush.nursery.dto;

import com.ayush.nursery.enums.PaymentMode;
import com.ayush.nursery.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ledger {

    private int sno;
    private int invoiceId;
    private String description;
    private double amount;

    private String date;


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

}
