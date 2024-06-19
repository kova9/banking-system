package com.bankingSystem.bankingSystem.provider;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.interfaces.ITransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MainProvider {
    private final ITransaction transactionInterface;

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = transactionInterface.findAll();

        return  transactions;
    }
}
