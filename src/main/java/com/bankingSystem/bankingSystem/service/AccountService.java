package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.AccountRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.dataaccess.sql.TransactionSql;
import com.bankingSystem.bankingSystem.dto.SearchDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<List<Account>> getTurnovers(){
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().minusMonths(1));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now());

        List<Account> accounts = accountRepository.findAll();
//        List<Customer> customers = customerRepository.findAll();
//        List<Account> accounts = new ArrayList<>();
//        customers.forEach(customer -> accounts.add(customer.getAccounts()));

        for(Account account : accounts){
            BigDecimal turnOver = calculateTurnover(account, startDate, endDate);
            account.setPastMonthTurnover(turnOver);
            accountRepository.save(account);
        }

        return ResponseEntity.ok().build();
    }

    private BigDecimal calculateTurnover(Account account, Timestamp startDate, Timestamp endDate){
        SearchDto searchDto = new SearchDto();

        searchDto.setSenderId(new ArrayList<>());
        searchDto.setReceiverId(new ArrayList<>());

        searchDto.getReceiverId().add(account.getAccountId());
        searchDto.getSenderId().add(account.getAccountId());
        searchDto.setStartDate(startDate);
        searchDto.setEndDate(endDate);
        searchDto.setSenderAndReceiverSame(true);
        searchDto.setStorno(false);

        Specification<Transaction> specification = new TransactionSql(searchDto);
        List<Transaction> transactions = transactionRepository.findAll(specification);
        BigDecimal turnOver = BigDecimal.ZERO;
        for(Transaction transaction : transactions){
            if(transaction.getSenderAccountId().equals(account.getAccountId())){
                turnOver = turnOver.subtract(transaction.getAmount());
            }else {
                turnOver = turnOver.add(transaction.getAmount());
            }
        }

        return turnOver;
    }
}
