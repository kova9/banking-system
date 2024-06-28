package com.bankingSystem.bankingSystem.account.dto;

import lombok.Data;

@Data
public class CreateAccountDto {
    private String customerId;
    private String accountType;
}
