package com.bankingSystem.bankingSystem.dataaccess.interfaces;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransaction extends JpaRepository<Transaction, String> {
}
