package com.ayush.nursery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "customer_detail")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    private String customerName;
    private String emailId;
    private String contactNo;
    private String address;
    private double balance;
    private String bankAccountNo;
    private String bankName;
    private String ifsc;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference("invoice-customer")
    private List<Invoice> invoiceList;

}
