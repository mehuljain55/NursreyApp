package com.ayush.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_detail")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     customerId;
    private String customerName;
    private String emailId;
    private String contactNo;
    private String address;
    private double balance;
}
