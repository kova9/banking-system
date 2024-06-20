package com.bankingSystem.bankingSystem.resource;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.provider.TransactionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class TransactionResource {
    private final TransactionProvider provider;

    @GetMapping("/transactions/all")
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
