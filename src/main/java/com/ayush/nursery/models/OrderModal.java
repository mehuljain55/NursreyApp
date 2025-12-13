package com.ayush.nursery.models;


import lombok.Data;

@Data
public class OrderModal {

    private String itemName;
    private int quantity;
    private double price;
    private double totalAmount;

}
