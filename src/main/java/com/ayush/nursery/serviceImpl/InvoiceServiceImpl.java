package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.repository.InvoiceRepository;
import com.ayush.nursery.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;

public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;



}
