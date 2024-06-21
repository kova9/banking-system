package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.logic.TransactionLogic;
import com.bankingSystem.bankingSystem.dataaccess.repository.AccountRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.dataaccess.sql.TransactionSql;
import com.bankingSystem.bankingSystem.enums.AccountId;
import com.bankingSystem.bankingSystem.enums.CustomerId;
import com.bankingSystem.bankingSystem.obj.TransactionDto;
import com.bankingSystem.bankingSystem.obj.TransactionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final AccountRepository accountRepository;

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

    public ResponseEntity<TransactionResponse> saveTransaction(JsonNode in) {
        TransactionDto dto = TransactionDto.fromJson(in);

        boolean isTransactionValid = (checkTransactionData(dto));
        if(!isTransactionValid){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Transaction newTransaction = TransactionLogic.getInstance().create(dto);
        transactionRepository.save(newTransaction);

        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(newTransaction.getTransactionId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean checkAccount(String accountId){
        AccountId accountEnum = AccountId.fromCode(accountId);
        if(accountEnum == null){
            return false;
        }

        Optional<Account> account = accountRepository.findById(accountEnum.getAccount());
        return account.isPresent();
    }

    private boolean checkTransactionData(TransactionDto dto){
        if(dto.getSenderAccountId().isEmpty() || dto.getReceiverAccountId().isEmpty() || dto.getAmount() == null || dto.getCurrencyId().isEmpty()){
            return false;
        }else {
            boolean isSenderValid = checkAccount(dto.getSenderAccountId());
            boolean isReceiverValid = checkAccount(dto.getReceiverAccountId());

            return isReceiverValid && isSenderValid;
        }
    }
}
