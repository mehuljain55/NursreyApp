package com.ayush.nursery.service;

import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.models.InvoiceModal;

public interface InvoiceService {

    int generateInvoiceNumber();

    ApiResponseModal createInvoice(InvoiceModal invoiceModal);


}
