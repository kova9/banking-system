package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountLogic {

    public Optional<Account> update(Optional<Account> oldAccount, AccountDto dto){

        oldAccount.get().setAccountNumber(dto.getAccountNumber());
        oldAccount.get().setBalance(dto.getBalance());
        oldAccount.get().setPastMonthTurnover(dto.getPastMonthTurnover());
        oldAccount.get().setAccountType(dto.getAccountType());

        return oldAccount;
    }
}
