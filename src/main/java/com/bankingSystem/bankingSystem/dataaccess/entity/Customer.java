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
@Table(name = "customer")
public class Customer {

    @Id
    @Column(length = 36)
    private String customerId;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String accounts;
}
