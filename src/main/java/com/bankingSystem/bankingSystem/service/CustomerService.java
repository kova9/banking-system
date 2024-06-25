package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.logic.CustomerLogic;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import com.bankingSystem.bankingSystem.dto.CustomerDto;
import com.bankingSystem.bankingSystem.enums.CustomerId;
import com.bankingSystem.bankingSystem.exception.BankingSystemException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bankingSystem.bankingSystem.exception.ExceptionMessages.*;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerLogic customerLogic;

    public CustomerService(CustomerRepository customerRepository, CustomerLogic customerLogic){
        this.customerRepository = customerRepository;
        this.customerLogic = customerLogic;
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
        Optional<Customer> customer;

        if(customerEnum == null){
            customer = customerRepository.findById(id);
        }else{
            customer = customerRepository.findById(customerEnum.getAccount());
        }
        if(customer.isEmpty()){
            throw BankingSystemException.notFound().message(ERROR_CUSTOMER_NOT_FOUND).build();
        }

        return ResponseEntity.ok(customer.get());
    }

    public ResponseEntity<Customer> createCustomer(JsonNode in) {
        CustomerDto dto = CustomerDto.fromJson(in);

        checkCustomerData(dto);
        Customer customer = customerLogic.create(dto);
        customerRepository.save(customer);

        return ResponseEntity.ok(customer);
    }

    private void checkCustomerData(CustomerDto dto){
        if(dto.getName().isEmpty() || dto.getAddress().isEmpty() || dto.getEmail().isEmpty() || dto.getPhoneNumber().isEmpty()){
            throw BankingSystemException.badRequest().message(ERROR_INVALID_DATE).build();
        }

        Customer customer = customerRepository.findByEmailAddress(dto.getEmail());
        if(customer != null){
            throw  BankingSystemException.badRequest().message(ERROR_EMAIL_ALREADY_TAKEN).build();
        }
    }
}
