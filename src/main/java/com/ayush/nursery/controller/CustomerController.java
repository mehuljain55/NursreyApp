package com.ayush.nursery.controller;

import com.ayush.nursery.dto.CustomerDto;
import com.ayush.nursery.dto.CustomerLedgerDto;
import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/findAll")
    public ApiResponseModal<List<CustomerDto>> findAllCustomerData()
    {
        List<CustomerDto> customerDtoList=customerService.findAllCustomers();

        if(customerDtoList.isEmpty()) {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"No customer found");
        }else {
            return new ApiResponseModal<>(StatusResponse.SUCCESS,customerDtoList,"Customer details found");
        }
    }

    @GetMapping("/ledger/customerId")
    public ApiResponseModal findCustomerLedger(@RequestParam("customerId") int customerId)
    {
        CustomerLedgerDto customerLedgerDto=customerService.findCustomerLedger(customerId);

        if(customerLedgerDto==null) {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"Invalid customer id");
        }else {
            return new ApiResponseModal<>(StatusResponse.SUCCESS,customerLedgerDto,"Customer ledger found");
        }
    }

    @GetMapping("/getDueBalance")
    public ApiResponseModal getCustomerDueBalance(@RequestParam("customerId") int customerId)
    {
        double dueBalance=customerService.calculateBalances(customerId);
        return new ApiResponseModal<>(StatusResponse.SUCCESS,dueBalance,"Customer balance found");
    }


}
