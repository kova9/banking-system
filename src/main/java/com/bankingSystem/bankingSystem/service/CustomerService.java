package com.bankingSystem.bankingSystem.service;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.dataaccess.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = new ArrayList<>(customerRepository.findAll());

        if (customers.isEmpty()){
            return new ResponseEntity<>(customers, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    public ResponseEntity<Customer> findById(String id){
        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    }
}
