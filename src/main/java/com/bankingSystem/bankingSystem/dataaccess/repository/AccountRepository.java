package com.bankingSystem.bankingSystem.dataaccess.repository;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
