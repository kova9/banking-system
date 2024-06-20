package com.bankingSystem.bankingSystem.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @Column(length = 36)
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
