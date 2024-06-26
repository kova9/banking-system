package com.bankingSystem.bankingSystem.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
//Turn on when debugging
@ToString(exclude = "accounts")
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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;
}
