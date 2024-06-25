package com.bankingSystem.bankingSystem.dataaccess.entity;

import com.bankingSystem.bankingSystem.dto.AccountDto;
import jakarta.persistence.*;
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

    @Column
    private String accountType;

    @Column
    private BigDecimal balance;

    @Column
    private BigDecimal pastMonthTurnover;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    public AccountDto toDto(){
        AccountDto dto = new AccountDto();

        dto.setAccountId(getAccountId());
        dto.setAccountNumber(getAccountNumber());
        dto.setAccountType(getAccountType());
        dto.setBalance(getBalance());
        dto.setPastMonthTurnover(getPastMonthTurnover());

        return dto;
    }
}
