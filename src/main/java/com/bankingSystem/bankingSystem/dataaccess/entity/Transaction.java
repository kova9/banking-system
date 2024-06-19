package com.bankingSystem.bankingSystem.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {

    @Id
    @Column
    private String transactionId;

    @Column
    private String senderAccountId;

    @Column
    private String receiverAccountId;

    @Column
    private BigDecimal amount;

    @Column
    private String currencyId;

    @Column
    private String message;

    @Column
    private LocalDateTime timestamp;
}
