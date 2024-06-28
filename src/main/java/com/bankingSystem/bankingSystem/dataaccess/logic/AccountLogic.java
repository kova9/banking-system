package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dto.AccountDto;
import com.bankingSystem.bankingSystem.enums.AccountType;
import com.bankingSystem.bankingSystem.util.AccountNumberUtil;
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
    public Account create(AccountDto dto){
        Account account = new Account();

        if(dto.getAccountId() == null){
            account.setAccountId(UUID.randomUUID().toString());
        }else{
            account.setAccountId(dto.getAccountId());
        }

        if(dto.getAccountType() == null){
            account.setAccountType(AccountType.CHECKING.getDescription());
        }else{
            account.setAccountType(AccountType.fromCode(dto.getAccountType()).getDescription());
        }

        if(dto.getCustomerId() != null ){
            Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
            customer.ifPresent(account::setCustomer);
        }
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
