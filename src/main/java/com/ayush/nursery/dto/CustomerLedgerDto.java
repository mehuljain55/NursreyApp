package com.ayush.nursery.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerLedgerDto {

    private double purchaseBalance;
    private double creditBalance;
    private double amountPaid;
    private double netBalance;
    private List<Ledger> ledgerList;

}
