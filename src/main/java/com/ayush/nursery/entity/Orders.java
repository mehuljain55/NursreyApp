package com.ayush.nursery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private String itemName;
    private int quantity;
    private double price;
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference("invoice-orders")
    private Invoice invoice;
}
