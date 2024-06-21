package com.bankingSystem.bankingSystem.dataaccess.repository;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
