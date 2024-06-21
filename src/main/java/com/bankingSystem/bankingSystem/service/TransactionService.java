package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.logic.AccountLogic;
import com.bankingSystem.bankingSystem.dataaccess.logic.TransactionLogic;
import com.bankingSystem.bankingSystem.dataaccess.repository.AccountRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.dataaccess.sql.TransactionSql;
import com.bankingSystem.bankingSystem.enums.AccountId;
import com.bankingSystem.bankingSystem.enums.CustomerId;
import com.bankingSystem.bankingSystem.obj.AccountDto;
import com.bankingSystem.bankingSystem.obj.EmailInfo;
import com.bankingSystem.bankingSystem.obj.TransactionDto;
import com.bankingSystem.bankingSystem.obj.TransactionResponse;
import com.fasterxml.jackson.databind.JsonNode;
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

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final EmailSenderService emailSenderService;
    private final AccountLogic accountLogic;
    private final TransactionLogic transactionLogic;

    // Constructor Injection
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository,
                              AccountRepository accountRepository, EmailSenderService emailSenderService,
                              AccountLogic accountLogic, TransactionLogic transactionLogic){

        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.emailSenderService = emailSenderService;
        this.accountLogic = accountLogic;
        this.transactionLogic = transactionLogic;
    }

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

        try{
            boolean isTransactionValid = (checkTransactionData(dto));
            if(!isTransactionValid){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Transaction newTransaction = transactionLogic.create(dto);
            transactionRepository.save(newTransaction);

            TransactionResponse response = new TransactionResponse();
            response.setTransactionId(newTransaction.getTransactionId());

            sendInfoMails(newTransaction);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendInfoMails(Transaction transaction){
        Optional<Account> senderAccount = accountRepository.findById(transaction.getSenderAccountId());
        Optional<Account> receiverAccount = accountRepository.findById(transaction.getReceiverAccountId());

        updateAccounts(senderAccount, transaction, false);
        updateAccounts(receiverAccount, transaction, true);

        EmailInfo senderInfo = createEmailInfo(transaction, senderAccount, false);
        EmailInfo receiverInfo = createEmailInfo(transaction, receiverAccount, true);

        String senderMail = customerRepository.findByAccountId(senderAccount.get().getAccountId()).getEmail();
        String receiverMail = customerRepository.findByAccountId(receiverAccount.get().getAccountId()).getEmail();

        emailSenderService.sendMail(senderMail, senderInfo);
        emailSenderService.sendMail(receiverMail, receiverInfo);
    }

    private void updateAccounts(Optional<Account> account, Transaction transaction, boolean isReceiver){
        AccountDto dto = account.get().toDto();

        if(isReceiver){
            dto.setBalance(dto.getBalance().add(transaction.getAmount()));
        }else{
            dto.setBalance(dto.getBalance().subtract(transaction.getAmount()));
        }

        Optional<Account> updatedAccount = accountLogic.update(account, dto);
        accountRepository.save(updatedAccount.get());
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
        if(dto.getSenderAccountId() == null || dto.getReceiverAccountId() == null || dto.getCurrencyId() == null ||
                dto.getSenderAccountId().isEmpty() || dto.getReceiverAccountId().isEmpty() || dto.getAmount() == null || dto.getCurrencyId().isEmpty()){

            return false;
        }else {
            boolean isSenderValid = checkAccount(dto.getSenderAccountId());
            boolean isReceiverValid = checkAccount(dto.getReceiverAccountId());

            return isReceiverValid && isSenderValid;
        }
    }

    private EmailInfo createEmailInfo(Transaction newTransaction, Optional<Account> account, boolean isReceiver){
        EmailInfo emailInfo = new EmailInfo();

        emailInfo.setTransactionId(newTransaction.getTransactionId());
        emailInfo.setAmount(newTransaction.getAmount());
        emailInfo.setOldBalance(account.get().getBalance());

        if(isReceiver){
            emailInfo.setNewBalance(account.get().getBalance().add(newTransaction.getAmount()));
            emailInfo.setType("added");
        }else{
            emailInfo.setNewBalance(account.get().getBalance().subtract(newTransaction.getAmount()));
            emailInfo.setType("deducted");
        }

        emailInfo.setCurrency(newTransaction.getCurrencyId());

        return emailInfo;
    }
}
