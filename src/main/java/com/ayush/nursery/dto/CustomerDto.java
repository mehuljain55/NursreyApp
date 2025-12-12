package com.ayush.nursery.dto;

import lombok.Builder;

@Builder
public class CustomerDto {

    private int customerId;
    private String customerName;
    private String emailId;
    private String contactNo;
    private String address;
    private double balance;

    private String bankAccountNo;
    private String bankName;
    private String ifsc;


}
