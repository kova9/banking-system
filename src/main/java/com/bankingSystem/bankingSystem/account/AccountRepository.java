package com.bankingSystem.bankingSystem.account;

import com.bankingSystem.bankingSystem.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
