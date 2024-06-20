package com.bankingSystem.bankingSystem.dataaccess.repository;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
}
