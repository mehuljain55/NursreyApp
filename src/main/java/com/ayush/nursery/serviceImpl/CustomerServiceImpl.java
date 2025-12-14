package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.dto.CustomerDto;
import com.ayush.nursery.dto.CustomerLedgerDto;
import com.ayush.nursery.dto.Ledger;
import com.ayush.nursery.entity.CreditHistory;
import com.ayush.nursery.entity.Customer;
import com.ayush.nursery.entity.Invoice;
import com.ayush.nursery.entity.Transactions;
import com.ayush.nursery.enums.PaymentMode;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.enums.TransactionType;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.repository.CreditHistoryRepository;
import com.ayush.nursery.repository.CustomerRepository;
import com.ayush.nursery.repository.InvoiceRepository;
import com.ayush.nursery.repository.TransactionRepository;
import com.ayush.nursery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditHistoryRepository creditHistoryRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

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
        }


        if(!isError) {
            customerRepository.save(customer);
            return new ApiResponseModal<>(StatusResponse.SUCCESS, null, "Customer data created");
        }

        return new ApiResponseModal<>(StatusResponse.FAILED, errorMessageList, "Errors in customer data");
    }

    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }


    public double calculateBalances(int customerId) {

        try {

            Optional<Customer> customerOptional = customerRepository.findById(customerId);

            if (customerOptional.isEmpty()) {
                return 0;
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
            return netBalance;

        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public CustomerLedgerDto findCustomerLedger(int customerId) {

        Optional<Customer> customerOptional=customerRepository.findById(customerId);

        if (!customerOptional.isEmpty()) {
            return null;
        }

        double purchaseBalance = 0;
        double creditBalance = 0;
        double amountPaid = 0;
        double netBalance;

        List<Ledger> result = new ArrayList<>();
        int sno = 1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<Invoice> invoiceList = invoiceRepository.findInvoices(customerId);
        List<Transactions> transactionsList = transactionRepository.findTransactions(customerId);
        List<CreditHistory> creditHistoryList = creditHistoryRepository.findByCustomerId(customerId);

    /* ==========================
       CALCULATE TOTALS (ONCE)
       ========================== */

        for (Invoice invoice : invoiceList) {
            purchaseBalance += invoice.getFinalAmount();
        }

        for (Transactions tx : transactionsList) {
            amountPaid += tx.getAmount();
        }

        for (CreditHistory credit : creditHistoryList) {
            creditBalance += credit.getAmount();
        }

    /* ==========================
       BUILD LEDGER INVOICE-WISE
       ========================== */

        for (Invoice invoice : invoiceList) {

            // ---------- Invoice Entry ----------
            Ledger invoiceLedger = new Ledger();
            invoiceLedger.setSno(sno++);
            invoiceLedger.setInvoiceId(invoice.getInvoiceId());
            invoiceLedger.setDescription("Invoice Generated");
            invoiceLedger.setAmount(invoice.getFinalAmount());
            invoiceLedger.setTransactionType(TransactionType.PURCHASE);
            invoiceLedger.setPaymentMode(PaymentMode.NA);
            invoiceLedger.setDate(dateFormat.format(invoice.getDate()));

            result.add(invoiceLedger);

            List<Ledger> invoiceLedgers = new ArrayList<>();

            // ---------- Credit Entries ----------
            for (CreditHistory credit : creditHistoryList) {

                if (credit.getInvoiceId() == invoice.getInvoiceId()) {

                    Date creditDateTime = mergeDateTime(
                            credit.getDate(),
                            credit.getTime()
                    );

                    Ledger l = new Ledger();
                    l.setInvoiceId(invoice.getInvoiceId());
                    l.setDescription(credit.getDescription());
                    l.setAmount(credit.getAmount());
                    l.setTransactionType(TransactionType.CREDIT);
                    l.setPaymentMode(PaymentMode.NA);
                    l.setSno((int) creditDateTime.getTime());
                    l.setDate(dateFormat.format(creditDateTime));

                    invoiceLedgers.add(l);
                }
            }

            // ---------- Transaction Entries ----------
            for (Transactions tx : transactionsList) {

                if (tx.getInvoiceId() == invoice.getInvoiceId()) {

                    Date txDateTime = mergeDateTime(
                            tx.getDate(),
                            tx.getTime()
                    );

                    Ledger l = new Ledger();
                    l.setInvoiceId(invoice.getInvoiceId());
                    l.setDescription(tx.getDescription());
                    l.setAmount(tx.getAmount());
                    l.setTransactionType(TransactionType.DEBIT);
                    l.setPaymentMode(tx.getPaymentMode());
                    l.setSno((int) txDateTime.getTime());
                    l.setDate(dateFormat.format(txDateTime));

                    invoiceLedgers.add(l);
                }
            }

            // ---------- Sort invoice-wise by date ----------
            invoiceLedgers.sort(Comparator.comparingInt(Ledger::getSno));

            // ---------- Assign final serial numbers ----------
            for (Ledger l : invoiceLedgers) {
                l.setSno(sno++);
                result.add(l);
            }
        }

    /* ==========================
       FINAL BALANCE LOGIC
       ========================== */

        netBalance = purchaseBalance - amountPaid;

        if (netBalance <= 0) {
            creditBalance = 0;
        }

        CustomerLedgerDto customerLedgerDto = new CustomerLedgerDto();
        customerLedgerDto.setPurchaseBalance(purchaseBalance);
        customerLedgerDto.setCreditBalance(creditBalance);
        customerLedgerDto.setAmountPaid(amountPaid);
        customerLedgerDto.setNetBalance(netBalance);
        customerLedgerDto.setLedgerList(result);

        return customerLedgerDto;
    }

    private Date mergeDateTime(Date date, Date time) {

        if (time == null) return date;

        Calendar d = Calendar.getInstance();
        d.setTime(date);

        Calendar t = Calendar.getInstance();
        t.setTime(time);

        d.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
        d.set(Calendar.SECOND, t.get(Calendar.SECOND));

        return d.getTime();
    }


    private CustomerDto convertToDto(Customer customer)
    {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .emailId(customer.getEmailId())
                .contactNo(customer.getContactNo())
                .address(customer.getAddress())
                .balance(customer.getBalance())
                .bankAccountNo(customer.getBankAccountNo())
                .bankName(customer.getBankName())
                .ifsc(customer.getIfsc())
                .build();
    }


}
