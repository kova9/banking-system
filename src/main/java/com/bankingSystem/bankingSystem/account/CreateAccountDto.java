package com.bankingSystem.bankingSystem.account;

import lombok.Data;

@Data
public class CreateAccountDto {
    private String customerId;
    private String accountType;
}
