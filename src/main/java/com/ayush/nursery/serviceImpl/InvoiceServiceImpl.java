package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.entity.CreditHistory;
import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.entity.Invoice;
import com.ayush.nursery.entity.Transactions;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.models.InvoiceModal;
import com.ayush.nursery.repository.CreditHistoryRepository;
import com.ayush.nursery.repository.CustomerRepository;
import com.ayush.nursery.repository.InvoiceRepository;
import com.ayush.nursery.repository.TransactionRepository;
import com.ayush.nursery.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditHistoryRepository creditHistoryRepository;

    @Autowired
    private CustomerRepository customerRepository;



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


    public ApiResponseModal createInvoice(InvoiceModal invoiceModal)
    {
        Optional<Customer> customerOptional=customerRepository.findById(invoiceModal.getCustomerId());

        if(customerOptional.isEmpty())
        {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"Customer not found");
        }
        return new ApiResponseModal<>(StatusResponse.FAILED,null,"Customer not found");
    }


    private void calculateBalances(int customerId) {

        try {

            Optional<Customer> customerOptional = customerRepository.findById(customerId);

            if (customerOptional.isEmpty()) {
                return;
            }

            Customer customer = customerOptional.get();

            double purchaseBalance = 0;
            double creditBalance = 0;
            double amountPaid = 0;

            List<Invoice> invoiceList = invoiceRepository.findInvoices(customerId);
            List<Transactions> transactionsList = transactionRepository.findTransactions(customerId);
            List<CreditHistory> creditHistoryList = creditHistoryRepository.findByCustomerId(customerId);

            for (Invoice invoice : invoiceList) {
                purchaseBalance += invoice.getFinalAmount();
            }

            for (Transactions tx : transactionsList) {
                amountPaid += tx.getAmount();
            }

            for (CreditHistory credit : creditHistoryList) {
                creditBalance += credit.getAmount();
            }

            double netBalance = purchaseBalance - amountPaid;
            customer.setBalance(netBalance);
            customerRepository.save(customer);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
