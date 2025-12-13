package com.ayush.nursery.models;

import com.ayush.nursery.enums.PaymentStatus;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceModal {

    private int invoiceId;
    private int customerId;
    private double amount;
    private double discount;
    private double finalAmount;
    private double dueAmount;
    private PaymentStatus paymentStatus; // FULL_PAYMENT, PARTIAL_PAYMENT, CREDIT

    private List<OrderModal> orderList;
    private List<PaymentModal> paymentList;  // leave empty if credit

}
