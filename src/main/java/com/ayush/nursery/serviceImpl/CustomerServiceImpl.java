package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.repository.CustomerRepository;
import com.ayush.nursery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ApiResponseModal createCustomer(Customer customer) {

        boolean isError = false;
        List<String> errorMessageList = new ArrayList<>();


        if (customer.getCustomerName() == null || customer.getCustomerName().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("Customer name is empty");
        }

        if (customer.getEmailId() != null && !customer.getEmailId().trim().isEmpty()) {
            if (!customer.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                isError = true;
                errorMessageList.add("Invalid email format");
            }
        }

        if (customer.getContactNo() != null && !customer.getContactNo().trim().isEmpty()) {
            if (!customer.getContactNo().matches("^[0-9]{10}$")) {
                isError = true;
                errorMessageList.add("Contact number must be 10 digits");
            }
        }

        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("Address is empty");
        }


        if (customer.getBankAccountNo() == null || customer.getBankAccountNo().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("Bank account number is empty");
        } else {
            if (!customer.getBankAccountNo().matches("^[0-9]{8,20}$")) {
                isError = true;
                errorMessageList.add("Bank account number should be 8–20 digits");
            }

            if (customerRepository.existsAccount(customer.getBankAccountNo())) {
                isError = true;
                errorMessageList.add("Customer bank account number already exists");
            }
        }

        if (customer.getBankName() == null || customer.getBankName().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("Bank name is empty");
        }

        if (customer.getIfsc() == null || customer.getIfsc().trim().isEmpty()) {
            isError = true;
            errorMessageList.add("IFSC code is empty");
        } else {
            if (!customer.getIfsc().matches("^[A-Z]{4}0[A-Z0-9]{6}$")) {
                isError = true;
                errorMessageList.add("Invalid IFSC code format");
            }
        }


        if(!isError) {
            customerRepository.save(customer);
            return new ApiResponseModal<>(StatusResponse.SUCCESS, null, "Customer data created");
        }

        return new ApiResponseModal<>(StatusResponse.FAILED, errorMessageList, "Errors in customer data");
    }


}
