package com.bankingSystem.bankingSystem.customer.repository;

import com.bankingSystem.bankingSystem.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c FROM Customer c JOIN c.accounts a WHERE a.accountId = :accountId")
    Customer findByAccountId(@Param("accountId") String accountId);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findByEmailAddress(@Param("email") String email);
}
