package com.bankingSystem.bankingSystem.transaction.controller;

import com.bankingSystem.bankingSystem.transaction.dto.TransactionDto;
import com.bankingSystem.bankingSystem.transaction.response.TransactionResponse;
import com.bankingSystem.bankingSystem.transaction.service.TransactionService;
import com.bankingSystem.bankingSystem.transaction.entity.Transaction;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Parameter(name = "senderAccountId", example = "cf3d5e48-1e8b-4c5c-877c-8d1b4239f489", required = true)
    @Parameter(name = "receiverAccountId", example = "ac0c4d5b-725e-4e0c-9a57-9d6184b7d5c7", required = true)
    @Parameter(name = "amount", example = "1000", required = true)
    @Parameter(name = "currencyId", example = "EUR", required = true)
    @Parameter(name = "message", example = "TestMessage", required = false)
    @Parameter(name = "storno", example = "false", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transaction to add", required = true, content = @Content(schema = @Schema(implementation = TransactionDto.class)))
    public ResponseEntity<TransactionResponse> saveTransaction(@RequestBody TransactionDto in){
        return transactionService.saveTransaction(in);
    }

    @GetMapping("/")
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
                                                                @RequestParam(required = false) String message,
                                                                @RequestParam(required = false) boolean storno)
                                                                {
        return transactionService.filterTransactions(customerId, startDate, endDate, currencyId, minAmount, maxAmount, message, storno);
    }

    @PostMapping("/storno/{id}")
    public ResponseEntity<TransactionResponse> stornateTransaction(@PathVariable("id") String id){
        return transactionService.stornateTransaction(id);
    }

}
