package com.bankingSystem.bankingSystem.dataaccess.repository;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c FROM Customer c WHERE c.account.accountId = :accountId")
    Customer findByAccountId(@Param("accountId") String accountId);
}
