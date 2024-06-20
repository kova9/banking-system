package com.bankingSystem.bankingSystem.provider;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.dataaccess.sql.TransactionSql;
import com.bankingSystem.bankingSystem.enums.CustomerId;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TransactionProvider {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<>(transactionRepository.findAll());

        if(transactions.isEmpty()){
            return new ResponseEntity<>(transactions, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    public ResponseEntity<List<Transaction>> filterTransactions(String customerId, Timestamp startDate, Timestamp endDate, BigDecimal minAmount, BigDecimal maxAmount) {
        try{
            CustomerId custId = CustomerId.fromCode(customerId);
            if(custId == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<Customer> customer = customerRepository.findById(custId.getAccount());
            if(customer.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String accountId = customer.get().getAccounts();

            Specification<Transaction> specification = new TransactionSql(accountId, startDate, endDate, minAmount, maxAmount);
            List<Transaction> transactions = transactionRepository.findAll(specification);

            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
