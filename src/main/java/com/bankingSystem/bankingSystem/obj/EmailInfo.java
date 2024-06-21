package com.bankingSystem.bankingSystem.obj;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmailInfo {

    private String transactionId;

    private BigDecimal amount;

    private BigDecimal oldBalance;

    private BigDecimal newBalance;

    private String type;

    private String currency;
}
