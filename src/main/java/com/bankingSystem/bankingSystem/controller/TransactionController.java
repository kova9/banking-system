package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.obj.TransactionResponse;
import com.bankingSystem.bankingSystem.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService provider;

    public TransactionController(TransactionService provider){
        this.provider = provider;
    }

    @PostMapping("/")
    public ResponseEntity<TransactionResponse> saveTransaction(@RequestBody JsonNode in){
        return provider.saveTransaction(in);
    }

    @GetMapping("/transaction/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return provider.getAllTransactions();
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Transaction>> filterTransactions(@PathVariable("customerId") String customerId,
                                                                @RequestParam(required = false) Timestamp startDate,
                                                                @RequestParam(required = false) Timestamp endDate,
                                                                @RequestParam(required = false) BigDecimal minAmount,
                                                                @RequestParam(required = false) BigDecimal maxAmount){
        return provider.filterTransactions(customerId, startDate, endDate, minAmount, maxAmount);
    }

}
