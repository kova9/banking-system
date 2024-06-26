package com.bankingSystem.bankingSystem.config;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public StartupApplicationListener(TransactionRepository transactionRepository, CustomerRepository customerRepository){
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(this::parseAndImportTransactionsFirstHalf);
        executorService.submit(this::parseAndImportTransactionsSecondHalf);
        executorService.submit(this::parseAndImportCustomers);

        executorService.shutdown();
    }

    public void parseAndImportTransactionsFirstHalf() {
        parseAndImportTransactions(0);
    }

    public void parseAndImportTransactionsSecondHalf() {
        parseAndImportTransactions(1);
    }

    Logger logger = Logger.getLogger(getClass().getName());


    public void parseAndImportTransactions(int part){
        try {
            String relativePath = "data/transactions.json";
            File file = new File(relativePath);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            ObjectReader reader = mapper.readerForListOf(Transaction.class);

            List<Transaction> transactions = reader.readValue(file);
            int size = transactions.size();
            int mid = size / 2;

            List<Transaction> subList;
            if (part == 0) {
                subList = transactions.subList(0, mid);
            } else {
                subList = transactions.subList(mid, size);
            }

            transactionRepository.saveAll(subList);
            if(!subList.isEmpty()){
                logger.info("Imported " + subList.size() + " accounts.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseAndImportCustomers() {
        try {
            String relativePath = "data/customers.json";
            File file = new File(relativePath);

            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerForListOf(Customer.class);

            List<Customer> customers = reader.readValue(file);

            for (Customer customer : customers) {
                for (Account account : customer.getAccounts()) {
                    account.setCustomer(customer);
                }
            }

            customerRepository.saveAll(customers);

            if (!customers.isEmpty()) {
                logger.info("Imported " + customers.size() + " customers.");
            }

        } catch (Exception e) {
            logger.info("Error importing customers: ");
        }
    }
}


