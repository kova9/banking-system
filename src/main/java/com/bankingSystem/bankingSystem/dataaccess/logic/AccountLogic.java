package com.bankingSystem.bankingSystem.dataaccess.logic;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.obj.AccountDto;

import java.util.Optional;

public class AccountLogic {
    private static final AccountLogic INSTANCE = new AccountLogic();

    private AccountLogic(){
    }

    public static AccountLogic getInstance(){return INSTANCE;}

    public Optional<Account> update(Optional<Account> oldAccount, AccountDto dto){

        oldAccount.get().setAccountNumber(dto.getAccountNumber());
        oldAccount.get().setBalance(dto.getBalance());
        oldAccount.get().setPastMonthTurnover(dto.getPastMonthTurnover());
        oldAccount.get().setAccountType(dto.getAccountType());

        return oldAccount;
    }
}
