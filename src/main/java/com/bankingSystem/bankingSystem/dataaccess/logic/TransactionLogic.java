package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.enums.AccountId;
import com.bankingSystem.bankingSystem.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionLogic {

    public Transaction create(TransactionDto dto){
        Transaction transaction = new Transaction();
        String senderAccount = getAccountId(dto.getSenderAccountId());
        String receiverAccount = getAccountId(dto.getReceiverAccountId());

        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(dto.getAmount());
        transaction.setMessage(dto.getMessage());
        transaction.setCurrencyId(dto.getCurrencyId());
        transaction.setSenderAccountId(senderAccount);
        transaction.setReceiverAccountId(receiverAccount);
        transaction.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        return transaction;
    }

    private String getAccountId(String accountId){
        AccountId accountEnum = AccountId.fromCode(accountId);
        String account;
        if(accountEnum == null){
            account = accountId;
        }else{
            account = accountEnum.getAccount();
        }

        return account;
    }
}
