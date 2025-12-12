package com.ayush.nursery.controller;

import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ApiResponseModal createCustomerData(@RequestBody Customer customer)
    {
        return customerService.createCustomer(customer);
    }

}
