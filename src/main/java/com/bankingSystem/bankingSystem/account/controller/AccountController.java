package com.bankingSystem.bankingSystem.account.controller;

import com.bankingSystem.bankingSystem.account.service.AccountService;
import com.bankingSystem.bankingSystem.account.dto.CreateAccountDto;
import com.bankingSystem.bankingSystem.account.entity.Account;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer to add", required = true, content = @Content(schema = @Schema(implementation = CreateAccountDto.class)))
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDto in){
        return accountService.createAccount(in);
    }
}
