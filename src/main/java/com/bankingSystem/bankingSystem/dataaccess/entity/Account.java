package com.bankingSystem.bankingSystem.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

    @Id
    @Column
    private String accountId;

    @Column
    private String accountNumber;

    // ?
    @Column
    private String accountType;

    @Column
    private BigDecimal balance;

    @Column
    private BigDecimal pastMonthTurnover;
}
