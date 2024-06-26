package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.service.AccountService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/")
    @Parameter(name = "customerId", example = "c3b36a50-5e4c-4e0b-9d8e-fd693f278f49", required = true)
    @Parameter(name = "accountType", example = "1", required = true)
    public ResponseEntity<Account> createAccount(@RequestBody JsonNode in){
        return accountService.createAccount(in);
    }
}
