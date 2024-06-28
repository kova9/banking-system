package com.bankingSystem.bankingSystem.transaction.service;

import com.bankingSystem.bankingSystem.account.entity.Account;
import com.bankingSystem.bankingSystem.customer.entity.Customer;
import com.bankingSystem.bankingSystem.account.logic.AccountLogic;
import com.bankingSystem.bankingSystem.account.repository.AccountRepository;
import com.bankingSystem.bankingSystem.customer.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.search.TransactionSql;
import com.bankingSystem.bankingSystem.account.dto.AccountDto;
import com.bankingSystem.bankingSystem.search.SearchDto;
import com.bankingSystem.bankingSystem.account.AccountId;
import com.bankingSystem.bankingSystem.customer.CustomerId;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import com.bankingSystem.bankingSystem.email.EmailInfo;
import com.bankingSystem.bankingSystem.email.EmailSenderService;
import com.bankingSystem.bankingSystem.transaction.response.TransactionResponse;
import com.bankingSystem.bankingSystem.transaction.dto.TransactionDto;
import com.bankingSystem.bankingSystem.transaction.entity.Transaction;
import com.bankingSystem.bankingSystem.transaction.logic.TransactionLogic;
import com.bankingSystem.bankingSystem.transaction.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.util.DateTimeUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bankingSystem.bankingSystem.exception.ExceptionMessages.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final EmailSenderService emailSenderService;
    private final AccountLogic accountLogic;
    private final TransactionLogic transactionLogic;

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

    private static final String ADDED = "added";
    private static final String DEDUCTED = "deducted";

    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<>(transactionRepository.findAll());

        if(transactions.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_NO_AVAILABLE_TRANSACTIONS).build();
        }

        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<List<Transaction>> filterTransactions(String customerId, String startDate, String endDate, String currencyId, BigDecimal minAmount, BigDecimal maxAmount, String message, boolean storno) {
        CustomerId customerEnum = CustomerId.fromCode(customerId);
        Optional<Customer> customer;
        if(customerEnum == null){
            customer = customerRepository.findById(customerId);
        }else{
            customer = customerRepository.findById(customerEnum.getAccount());
        }
        if(customer.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_CUSTOMER_NOT_FOUND).build();
        }

        List<String> accountIds = new ArrayList<>();
        customer.get().getAccounts().forEach(acc -> accountIds.add(acc.getAccountId()));
        SearchDto searchDto = new SearchDto();

        searchDto.setSenderId(accountIds);
        searchDto.setReceiverId(accountIds);
        searchDto.setCurrencyId(currencyId);
        searchDto.setMinAmount(minAmount);
        searchDto.setMaxAmount(maxAmount);
        searchDto.setSenderAndReceiverSame(true);
        searchDto.setMessage(message);
        searchDto.setStorno(storno);
        try{
            if(startDate != null){
                searchDto.setStartDate(DateTimeUtil.stringToTimestamp(startDate));
            }
            if(endDate != null){
                searchDto.setEndDate(DateTimeUtil.stringToTimestamp(endDate));
            }
        }catch (IllegalArgumentException e){
            throw BankingSystemException.badRequest().message(ERROR_INVALID_DATE).build();
        }

        Specification<Transaction> specification = new TransactionSql(searchDto);
        List<Transaction> transactions = transactionRepository.findAll(specification);

        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<TransactionResponse> saveTransaction(TransactionDto dto) {
        checkTransactionData(dto);

        Transaction newTransaction = transactionLogic.create(dto);
        transactionRepository.save(newTransaction);

        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(newTransaction.getTransactionId());

        sendInfoMails(newTransaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    private void sendInfoMails(Transaction transaction){
        Optional<Account> senderAccount = accountRepository.findById(transaction.getSenderAccountId());
        Optional<Account> receiverAccount = accountRepository.findById(transaction.getReceiverAccountId());

        if(senderAccount.isEmpty() || receiverAccount.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_ACCOUNT_NOT_FOUND).build();
        }

        updateAccount(senderAccount, transaction, false);
        updateAccount(receiverAccount, transaction, true);

        EmailInfo senderInfo = createEmailInfo(transaction, senderAccount, false);
        EmailInfo receiverInfo = createEmailInfo(transaction, receiverAccount, true);

        String senderMail = customerRepository.findByAccountId(senderAccount.get().getAccountId()).getEmail();
        String receiverMail = customerRepository.findByAccountId(receiverAccount.get().getAccountId()).getEmail();

        emailSenderService.sendMail(senderMail, senderInfo);
        emailSenderService.sendMail(receiverMail, receiverInfo);
    }

    private void updateAccount(Optional<Account> account, Transaction transaction, boolean isReceiver){
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
        Optional<Account> account;
        if(accountEnum == null){
            account = accountRepository.findById(accountId);
        }else{
            account = accountRepository.findById(accountEnum.getAccount());
        }

        return account.isPresent();
    }

    private void checkTransactionData(TransactionDto dto){
        if(dto.getSenderAccountId() == null || dto.getReceiverAccountId() == null || dto.getCurrencyId() == null ||
                dto.getSenderAccountId().isEmpty() || dto.getReceiverAccountId().isEmpty() || dto.getAmount() == null || dto.getCurrencyId().isEmpty()){

            throw BankingSystemException.badRequest().message(ERROR_REQUEST_DATA_NOT_COMPLETE).build();
        }else {
            boolean isSenderValid = checkAccount(dto.getSenderAccountId());
            boolean isReceiverValid = checkAccount(dto.getReceiverAccountId());

            if(!isReceiverValid || !isSenderValid){
                throw BankingSystemException.badRequest().message(ERROR_SENDER_OR_RECEIVER_ACCOUNT_NOT_FOUND).build();
            }
        }
    }

    private EmailInfo createEmailInfo(Transaction newTransaction, Optional<Account> account, boolean isReceiver){
        EmailInfo emailInfo = new EmailInfo();

        emailInfo.setTransactionId(newTransaction.getTransactionId());
        emailInfo.setAmount(newTransaction.getAmount());
        emailInfo.setOldBalance(account.get().getBalance());

        if(isReceiver){
            emailInfo.setNewBalance(account.get().getBalance().add(newTransaction.getAmount()));
            emailInfo.setType(ADDED);
        }else{
            emailInfo.setNewBalance(account.get().getBalance().subtract(newTransaction.getAmount()));
            emailInfo.setType(DEDUCTED);
        }

        emailInfo.setCurrency(newTransaction.getCurrencyId());

        return emailInfo;
    }

    public ResponseEntity<TransactionResponse> stornateTransaction(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_TRANSACTION_DOESNT_EXIST).build();
        }

        Optional<Account> senderAccount = accountRepository.findById(transaction.get().getSenderAccountId());
        Optional<Account> receiverAccount = accountRepository.findById(transaction.get().getReceiverAccountId());

        if(senderAccount.isEmpty() || receiverAccount.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_ACCOUNT_NOT_FOUND).build();
        }

        TransactionDto transactionDto = transaction.get().toDto();
        transactionDto.setStorno(true);
        transactionLogic.update(transaction.get(), transactionDto);

        reverseTransactions(senderAccount, transaction, false);
        reverseTransactions(receiverAccount, transaction, false);

        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.get().getTransactionId());

        return ResponseEntity.ok(response);
    }

    private void reverseTransactions(Optional<Account> account, Optional<Transaction> transaction, boolean isReceiver){
        AccountDto dto = account.get().toDto();
        if(!isReceiver){
            dto.setBalance(account.get().getBalance().add(transaction.get().getAmount()));
        }else{
            dto.setBalance(account.get().getBalance().subtract(transaction.get().getAmount()));
        }

        Optional<Account> updatedAccount = accountLogic.update(account, dto);
        accountRepository.save(updatedAccount.get());
    }
}
