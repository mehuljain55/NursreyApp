package com.ayush.nursery.controller;

import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate/invoiceId")
    public ApiResponseModal generateInvoiceNumber()
    {
        return new ApiResponseModal<>(StatusResponse.SUCCESS,invoiceService.generateInvoiceNumber(),"Invoice number generated");
    }

}
