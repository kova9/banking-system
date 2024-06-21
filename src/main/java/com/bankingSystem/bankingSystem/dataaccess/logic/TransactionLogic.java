package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.enums.AccountId;
import com.bankingSystem.bankingSystem.obj.TransactionDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionLogic {

    private static final TransactionLogic INSTANCE = new TransactionLogic();

    private TransactionLogic(){
    }

    public static TransactionLogic getInstance(){return INSTANCE;}

    public Transaction create(TransactionDto dto){
        Transaction transaction = new Transaction();

        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(dto.getAmount());
        transaction.setMessage(dto.getMessage());
        transaction.setCurrencyId(dto.getCurrencyId());
        transaction.setSenderAccountId(AccountId.fromCode(dto.getSenderAccountId()).getAccount());
        transaction.setReceiverAccountId(AccountId.fromCode(dto.getReceiverAccountId()).getAccount());
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }
}
