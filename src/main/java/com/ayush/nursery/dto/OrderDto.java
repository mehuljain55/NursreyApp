package com.ayush.nursery.dto;

import lombok.Data;

@Data
public class OrderDto {

    private int orderId;
    private String itemName;
    private int quantity;
    private double price;
    private double totalAmount;


}
