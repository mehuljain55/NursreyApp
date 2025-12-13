package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.repository.InvoiceRepository;
import com.ayush.nursery.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;

public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;


    @Override
    public int generateInvoiceNumber() {
        Integer invoiceId=invoiceRepository.findMaxInvoiceId();

        if(invoiceId==null)
        {
            invoiceId=1;
        }else {
            invoiceId+=1;
        }
        return invoiceId;
    }
}
