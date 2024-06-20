package com.bankingSystem.bankingSystem.provider;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.AccountRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.dataaccess.sql.TransactionSql;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AccountProvider {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public ResponseEntity<List<Account>> getTurnovers(Timestamp startDate, Timestamp endDate){
        List<Account> accounts = new ArrayList<>(accountRepository.findAll());

        for(Account account : accounts){
            BigDecimal turnOver = calculateTurnover(account, startDate, endDate);
            account.setPastMonthTurnover(turnOver);
            accountRepository.save(account);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private BigDecimal calculateTurnover(Account account, Timestamp startDate, Timestamp endDate){
        BigDecimal minAmount = null;
        BigDecimal maxAmount = null;
        Specification<Transaction> specification = new TransactionSql(account.getAccountId(), startDate, endDate, minAmount, maxAmount);
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
