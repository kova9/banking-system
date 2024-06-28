package com.bankingSystem.bankingSystem.customer.logic;

import com.bankingSystem.bankingSystem.customer.dto.CustomerDto;
import com.bankingSystem.bankingSystem.customer.entity.Customer;
import com.bankingSystem.bankingSystem.account.logic.AccountLogic;
import com.bankingSystem.bankingSystem.account.dto.CreateAccountDto;
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
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setCustomerId(customer.getCustomerId());
        customer.setAccounts(List.of(accountLogic.create(createAccountDto)));

        return customer;
    }
}
