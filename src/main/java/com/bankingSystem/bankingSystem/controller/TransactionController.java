package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.obj.TransactionResponse;
import com.bankingSystem.bankingSystem.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService provider){
        this.transactionService = provider;
    }

    @PostMapping("/")
    public ResponseEntity<TransactionResponse> saveTransaction(@RequestBody JsonNode in){
        return transactionService.saveTransaction(in);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Transaction>> filterTransactions(@PathVariable("customerId") String customerId,
                                                                @RequestParam(required = false) String startDate,
                                                                @RequestParam(required = false) String endDate,
                                                                @RequestParam(required = false) String currencyId,
                                                                @RequestParam(required = false) BigDecimal minAmount,
                                                                @RequestParam(required = false) BigDecimal maxAmount,
                                                                @RequestParam(required = false) String message){
        return transactionService.filterTransactions(customerId, startDate, endDate, currencyId, minAmount, maxAmount, message);
    }

}
