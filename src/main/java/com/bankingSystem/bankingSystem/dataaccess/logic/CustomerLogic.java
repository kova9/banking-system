package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dto.AccountDto;
import com.bankingSystem.bankingSystem.dto.CustomerDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerLogic {

    private final AccountLogic accountLogic;

    public CustomerLogic(AccountLogic accountLogic){
        this.accountLogic = accountLogic;
    }
    public Customer create(CustomerDto dto){
        Customer customer = new Customer();

        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setPhoneNumber(dto.getPhoneNumber());
//        customer.setAccount(accountLogic.create(new AccountDto()));
        customer.setAccounts(List.of(accountLogic.create(new AccountDto())));

        return customer;
    }
}
