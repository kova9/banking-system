package com.bankingSystem.bankingSystem.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Customer {

    @Id
    @Column
    private String customerId;

    @Column
    private String name;

    // ?
    @Column
    private String address;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String accounts;
}
