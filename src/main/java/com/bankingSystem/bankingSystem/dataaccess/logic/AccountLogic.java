package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dto.AccountDto;
import com.bankingSystem.bankingSystem.util.AccountNumberUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccountLogic {

    public Account create(AccountDto dto){
        Account account = new Account();

        if(dto.getAccountId() == null){
            account.setAccountId(UUID.randomUUID().toString());
        }else{
            account.setAccountId(dto.getAccountId());
        }

        account.setAccountType("Checking");
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
