package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.entity.*;
import com.ayush.nursery.enums.PaymentMode;
import com.ayush.nursery.enums.PaymentStatus;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.models.InvoiceModal;
import com.ayush.nursery.models.OrderModal;
import com.ayush.nursery.models.PaymentModal;
import com.ayush.nursery.repository.CreditHistoryRepository;
import com.ayush.nursery.repository.CustomerRepository;
import com.ayush.nursery.repository.InvoiceRepository;
import com.ayush.nursery.repository.TransactionRepository;
import com.ayush.nursery.service.InvoiceService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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

        if(invoiceModal.getOrderList().isEmpty())
        {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"No items found");
        }

        Customer customer=customerOptional.get();

        Invoice invoice=new Invoice();
        invoice.setInvoiceId(invoiceModal.getInvoiceId());
        invoice.setDate(new Date());
        invoice.setTime(new Date());
        invoice.setPaymentStatus(invoiceModal.getPaymentStatus());

        List<Orders> orderList=new ArrayList<>();

        double totalAmount=0;
        Double discount=invoiceModal.getDiscount();

        if(discount==null)
        {
            discount=0.0;
        }

        for(OrderModal orderModal:invoiceModal.getOrderList())
        {
            double price=orderModal.getPrice();
            double quantity=orderModal.getQuantity();
            double amount=price*quantity;
            totalAmount+=amount;
            Orders orders=new Orders();
            orders.setItemName(orderModal.getItemName());
            orders.setPrice(orderModal.getPrice());
            orders.setQuantity(orderModal.getQuantity());
            orders.setTotalAmount(amount);
            orders.setInvoice(invoice);
            orderList.add(orders);
        }

        invoice.setAmount(totalAmount);
        invoice.setDiscount(invoiceModal.getDiscount());

        Double finalAmount=totalAmount-discount;

        invoice.setFinalAmount(finalAmount);
        invoice.setOrdersList(orderList);
        invoiceRepository.save(invoice);
        createTransactionInvoice(invoice,customer.getCustomerId(),invoiceModal.getPaymentList());
        return new ApiResponseModal<>(StatusResponse.SUCCESS,null,"Invoice created");
    }


    private void createTransactionInvoice(Invoice invoice,int customerId, List<PaymentModal> paymentList)
    {
        List<Transactions> transactionsList=new ArrayList<>();

        double invoiceAmount=invoice.getFinalAmount();
        double creditAmount=0;
        double paidAmount=0;

        for(PaymentModal payment:paymentList)
        {
            if(payment.getPaymentMode().equalsIgnoreCase("CREDIT"))
            {
                CreditHistory creditHistory=new CreditHistory();
                creditHistory.setInvoiceId(invoice.getInvoiceId());
                creditHistory.setCustomerId(customerId);
                creditHistory.setDescription("Credit of invoice:"+invoice.getInvoiceId());
                creditHistory.setDate(new Date());
                creditHistory.setTime(new Date());
                creditHistory.setAmount(payment.getAmount());
                creditAmount=+creditHistory.getAmount();
                creditHistoryRepository.save(creditHistory);
            }

            if(payment.getPaymentMode().equalsIgnoreCase("CASH"))
            {
                Transactions transactions=new Transactions();
                transactions.setInvoiceId(invoice.getInvoiceId());
                transactions.setCustomerId(customerId);
                transactions.setAmount(payment.getAmount());
                transactions.setDescription("Payment received against invoice no-"+invoice.getInvoiceId());
                transactions.setDate(new Date());
                transactions.setTime(new Date());
                transactions.setPaymentMode(PaymentMode.CASH);
                paidAmount+=transactions.getAmount();
                transactionRepository.save(transactions);
            }

            if(payment.getPaymentMode().equalsIgnoreCase("UPI"))
            {
                Transactions transactions=new Transactions();
                transactions.setInvoiceId(invoice.getInvoiceId());
                transactions.setCustomerId(customerId);
                transactions.setAmount(payment.getAmount());
                transactions.setDescription("Payment received against invoice no-"+invoice.getInvoiceId());
                transactions.setDate(new Date());
                transactions.setTime(new Date());
                transactions.setPaymentMode(PaymentMode.UPI);
                paidAmount+=transactions.getAmount();

                transactionRepository.save(transactions);
            }

            if(payment.getPaymentMode().equalsIgnoreCase("BANK_TRANSFER") || payment.getPaymentMode().equalsIgnoreCase("BANK TRANSFER") )
            {
                Transactions transactions=new Transactions();
                transactions.setInvoiceId(invoice.getInvoiceId());
                transactions.setCustomerId(customerId);
                transactions.setAmount(payment.getAmount());
                transactions.setDescription("Payment received against invoice no-"+invoice.getInvoiceId());
                transactions.setDate(new Date());
                transactions.setTime(new Date());
                transactions.setPaymentMode(PaymentMode.BANK_TRANSFER);
                paidAmount+=transactions.getAmount();
                transactionRepository.save(transactions);
            }

            if(payment.getPaymentMode().equalsIgnoreCase("CHEQUE"))
            {
                Transactions transactions=new Transactions();
                transactions.setInvoiceId(invoice.getInvoiceId());
                transactions.setCustomerId(customerId);
                transactions.setAmount(payment.getAmount());
                transactions.setDescription("Payment received against invoice no-"+invoice.getInvoiceId());
                transactions.setDate(new Date());
                transactions.setTime(new Date());
                transactions.setPaymentMode(PaymentMode.CHEQUE);
                paidAmount+=transactions.getAmount();
                transactionRepository.save(transactions);
            }

        }

        double netAmount=invoiceAmount-paidAmount;

        if(netAmount<=0)
        {
            invoice.setDueAmount(0);
            invoice.setPaymentStatus(PaymentStatus.FULL_PAYMENT);
            invoiceRepository.save(invoice);
            return;
        }

      else if(paidAmount==0 && (creditAmount==netAmount))
        {
            invoice.setDueAmount(creditAmount);
            invoice.setPaymentStatus(PaymentStatus.CREDIT);
            invoiceRepository.save(invoice);
            return;
        }

      else  if(netAmount!=0 && paidAmount!=0 && creditAmount!=0)
        {
            invoice.setDueAmount(creditAmount);
            invoice.setPaymentStatus(PaymentStatus.PARTIAL_PAYMENT);
            invoiceRepository.save(invoice);
            return;
        }
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
