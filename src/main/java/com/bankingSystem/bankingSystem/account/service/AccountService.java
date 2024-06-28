package com.bankingSystem.bankingSystem.account.service;

import com.bankingSystem.bankingSystem.account.dto.CreateAccountDto;
import com.bankingSystem.bankingSystem.account.entity.Account;
import com.bankingSystem.bankingSystem.account.logic.AccountLogic;
import com.bankingSystem.bankingSystem.account.repository.AccountRepository;
import com.bankingSystem.bankingSystem.customer.entity.Customer;
import com.bankingSystem.bankingSystem.transaction.entity.Transaction;
import com.bankingSystem.bankingSystem.customer.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.transaction.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.search.TransactionSql;
import com.bankingSystem.bankingSystem.search.SearchDto;
import com.bankingSystem.bankingSystem.account.AccountType;
import com.bankingSystem.bankingSystem.customer.CustomerId;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bankingSystem.bankingSystem.exception.ExceptionMessages.*;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountLogic accountLogic;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository,
                          AccountLogic accountLogic){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountLogic = accountLogic;
    }

    public ResponseEntity<List<Account>> getTurnovers(){
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().minusMonths(1));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now());

        List<Account> accounts = accountRepository.findAll();

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

    public ResponseEntity<Account> createAccount(CreateAccountDto dto) {
        checkAccountData(dto);
        Account account = accountLogic.create(dto);

        accountRepository.save(account);

        return ResponseEntity.ok(account);
    }

    private void checkAccountData(CreateAccountDto accountDto){
        if(accountDto.getCustomerId().isEmpty()){
            throw BankingSystemException.badRequest().message(ERROR_CUSTOMER_FIELD_IS_MANDATORY).build();
        }else{
            CustomerId customerEnum = CustomerId.fromCode(accountDto.getCustomerId());
            if(customerEnum == null){
                Optional<Customer> customer = customerRepository.findById(accountDto.getCustomerId());
                if(customer.isEmpty()){
                    throw BankingSystemException.notFound().message(ERROR_CUSTOMER_NOT_FOUND).build();
                }
            }
        }
        if(accountDto.getAccountType().isEmpty()){
            throw BankingSystemException.badRequest().message(ERROR_ACCOUNT_CANT_BE_EMTPY).build();
        }else{
            AccountType accountType = AccountType.fromCode(accountDto.getAccountType());
            if(accountType == null){
                throw BankingSystemException.badRequest().message(ERROR_ACCOUNT_TYPE_DOESNT_EXIST).build();
            }
        }

    }
}
