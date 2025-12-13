package com.ayush.nursery.entity;

import com.ayush.nursery.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceId;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIME)
    private Date time;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private double amount;
    private double discount;
    private double finalAmount;
    private double dueAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference("invoice-orders")
    private List<Orders> ordersList;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonManagedReference("invoice-customer")
    private Customer customer;
}
