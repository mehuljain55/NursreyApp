package com.ayush.nursery.models;

import lombok.Data;

@Data
public class PaymentModal {

    private double amount;
    private String paymentMode; // CASH, UPI, BANK_TRANSFER, CHEQUE, CREDIT,
}
