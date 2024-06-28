package com.bankingSystem.bankingSystem.dto;

import lombok.Data;

@Data
public class CreateAccountDto {
    private String customerId;
    private String accountType;
}
