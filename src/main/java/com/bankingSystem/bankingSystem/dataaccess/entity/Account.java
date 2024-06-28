package com.bankingSystem.bankingSystem.dataaccess.entity;

import com.bankingSystem.bankingSystem.dto.AccountDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
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
    @JsonBackReference
    private Customer customer;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPastMonthTurnover() {
        return pastMonthTurnover;
    }

    public void setPastMonthTurnover(BigDecimal pastMonthTurnover) {
        this.pastMonthTurnover = pastMonthTurnover;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", pastMonthTurnover=" + pastMonthTurnover +
                ", customer=" + customer +
                '}';
    }

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
