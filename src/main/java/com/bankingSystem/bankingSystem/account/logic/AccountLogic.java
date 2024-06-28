package com.bankingSystem.bankingSystem.account.logic;

import com.bankingSystem.bankingSystem.account.dto.AccountDto;
import com.bankingSystem.bankingSystem.account.dto.CreateAccountDto;
import com.bankingSystem.bankingSystem.account.entity.Account;
import com.bankingSystem.bankingSystem.customer.entity.Customer;
import com.bankingSystem.bankingSystem.customer.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.account.AccountType;
import com.bankingSystem.bankingSystem.account.AccountNumberUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccountLogic {

    private final CustomerRepository customerRepository;
    public AccountLogic(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    public Account create(CreateAccountDto dto){
        Account account = new Account();

        account.setAccountId(UUID.randomUUID().toString());
        if(dto.getAccountType() == null){
            account.setAccountType(AccountType.CHECKING.getDescription());
        }else{
            account.setAccountType(AccountType.fromCode(dto.getAccountType()).getDescription());
        }

        Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
        customer.ifPresent(account::setCustomer);

        account.setAccountNumber(AccountNumberUtil.generateRandomAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setPastMonthTurnover(BigDecimal.ZERO);

        return account;
    }

    public Optional<Account> update(Optional<Account> oldAccount, AccountDto dto){

        oldAccount.get().setAccountNumber(dto.getAccountNumber());
        oldAccount.get().setBalance(dto.getBalance());
        oldAccount.get().setPastMonthTurnover(dto.getPastMonthTurnover());
        oldAccount.get().setAccountType(dto.getAccountType());

        return oldAccount;
    }
}
