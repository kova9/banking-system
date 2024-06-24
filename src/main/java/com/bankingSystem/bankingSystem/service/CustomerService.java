package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.enums.CustomerId;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bankingSystem.bankingSystem.exception.ExceptionMessages.ERROR_CUSTOMER_NOT_FOUND;
import static com.bankingSystem.bankingSystem.exception.ExceptionMessages.ERROR_NO_AVAILABLE_CUSTOMERS;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = new ArrayList<>(customerRepository.findAll());

        if (customers.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_NO_AVAILABLE_CUSTOMERS).build();
        }

        return ResponseEntity.ok(customers);
    }

    public ResponseEntity<Customer> findById(String id){
        CustomerId customerEnum = CustomerId.fromCode(id);
        if(customerEnum == null){
            throw BankingSystemException.notFound().message(ERROR_CUSTOMER_NOT_FOUND).build();
        }

        Optional<Customer> customer = customerRepository.findById(customerEnum.getAccount());
        if(customer.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_CUSTOMER_NOT_FOUND).build();
        }

        return ResponseEntity.ok(customer.get());
    }
}
