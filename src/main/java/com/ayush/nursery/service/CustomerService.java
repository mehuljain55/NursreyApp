package com.ayush.nursery.service;

import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.models.ApiResponseModal;

public interface CustomerService {

    ApiResponseModal createCustomer(Customer customer);

}
