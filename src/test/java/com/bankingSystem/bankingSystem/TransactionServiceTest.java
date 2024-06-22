package com.bankingSystem.bankingSystem;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dataaccess.repository.TransactionRepository;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import com.bankingSystem.bankingSystem.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTransactions_NotEmpty() {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        when(transactionRepository.findAll()).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionService.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testGetAllTransactions_Empty() {
        when(transactionRepository.findAll()).thenReturn(new ArrayList<>());

        BankingSystemException exception = assertThrows(BankingSystemException.class, () -> {
            transactionService.getAllTransactions();
        });
        assertEquals("No transactions available", exception.getMessage());
    }
}
