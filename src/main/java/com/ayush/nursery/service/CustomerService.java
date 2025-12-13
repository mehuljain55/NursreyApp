package com.ayush.nursery.service;

import com.ayush.nursery.dto.CustomerDto;
import com.ayush.nursery.dto.CustomerLedgerDto;
import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.models.ApiResponseModal;

import java.util.List;

public interface CustomerService {

    ApiResponseModal createCustomer(Customer customer);

    List<CustomerDto> findAllCustomers();

    CustomerLedgerDto findCustomerLedger(int customerId);

}
