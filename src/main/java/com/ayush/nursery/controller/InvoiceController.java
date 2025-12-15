package com.ayush.nursery.controller;

import com.ayush.nursery.dto.InvoiceDto;
import com.ayush.nursery.enums.PaymentMode;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.models.InvoiceModal;
import com.ayush.nursery.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    @PostMapping("/create")
    public ApiResponseModal createInvoice(@RequestBody InvoiceModal invoiceModal)
    {
        return invoiceService.createInvoice(invoiceModal);
    }

    @GetMapping("/view")
    public ApiResponseModal<InvoiceDto> viewInvoice(@RequestParam("invoiceId") int invoiceId)
    {
        return invoiceService.viewInvoice(invoiceId);
    }

    @GetMapping("/viewAll")
    public ApiResponseModal<List<InvoiceDto>> viewInvoice()
    {
        return invoiceService.viewAllInvoice();
    }

    @GetMapping("/today/view")
    public ApiResponseModal<List<InvoiceDto>> viewTodayInvoices()
    {
        return invoiceService.viewTodayInvoices();
    }


    @GetMapping("/sales/dateRange")
    public ApiResponseModal<Double> findSalesByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Date startUtilDate = sdf.parse(startDate);
            Date endUtilDate = sdf.parse(endDate);

            double amount = invoiceService.findSumByDateRange(startUtilDate, endUtilDate);

            return new ApiResponseModal<>(StatusResponse.SUCCESS, amount, "Sales found");

        } catch (ParseException e) {
            return new ApiResponseModal<>(StatusResponse.FAILED, null, "Invalid date format");
        }
    }



    @PostMapping("/customerRepayment")
    public ApiResponseModal customerRepayment(@RequestParam("customerId") int customerId,
                                              @RequestParam("amount") double amount,
                                              @RequestParam("paymentMode") PaymentMode paymentMode,
                                              @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        return invoiceService.addCustomerRepayment(customerId,amount,date,paymentMode);
    }


}
