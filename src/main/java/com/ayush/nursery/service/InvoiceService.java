package com.ayush.nursery.service;

import com.ayush.nursery.dto.InvoiceDto;
import com.ayush.nursery.enums.PaymentMode;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.models.InvoiceModal;

import java.util.Date;
import java.util.List;

public interface InvoiceService {

    int generateInvoiceNumber();

    ApiResponseModal createInvoice(InvoiceModal invoiceModal);

    ApiResponseModal<InvoiceDto> viewInvoice(int invoiceId);

    ApiResponseModal<List<InvoiceDto>> viewAllInvoice();

    ApiResponseModal addCustomerRepayment(int customerId, double amount, Date date, PaymentMode paymentMode);
}
