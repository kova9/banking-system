package com.bankingSystem.bankingSystem.config;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.AccountRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public StartupApplicationListener(TransactionRepository transactionRepository, CustomerRepository customerRepository, AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        parseAndImportTransactions();
        parseAndImportAccounts();
        parseAndImportCustomers();
    }

    Logger logger = Logger.getLogger(getClass().getName());

    public void parseAndImportTransactions() {
        try {
            File file = new File("data/transactions.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            ObjectReader reader = mapper.readerFor(Transaction.class);

            List<Transaction> transactions;
            try (var lines = Files.lines(Paths.get(file.toURI()))) {
                transactions = lines.map(line -> {
                    try {
                        return reader.readValue(line, Transaction.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
            }

            transactionRepository.saveAll(transactions);
            if(!transactions.isEmpty()){
                logger.info("Imported " + transactions.size() + " transactions.");
            }

        } catch (Exception e) {
            BankingSystemException.internalServerError().message(e.getMessage());
        }
    }

    public void parseAndImportAccounts(){
        try {
            String relativePath = "data/accounts.json";
            File file = new File(relativePath);

            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerForListOf(Account.class);

            List<Account> accounts = reader.readValue(file);

            accountRepository.saveAll(accounts);
            if(!accounts.isEmpty()){
                logger.info("Imported " + accounts.size() + " accounts.");
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

            customerRepository.saveAll(customers);
            if(!customers.isEmpty()){
                logger.info("Imported " + customers.size() + " customers.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

