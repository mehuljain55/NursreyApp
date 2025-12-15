package com.ayush.nursery.dto;

import com.ayush.nursery.enums.PaymentStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InvoiceDto {

    private int invoiceId;
    private String customerName;
    private String customerNumber;
    private String description;

    private String date;
    private String time;
    private PaymentStatus paymentStatus;
    private double amount;
    private double discount;
    private double finalAmount;
    private double dueAmount;
    private List<OrderDto> orderList;
}
