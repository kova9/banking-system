package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.service.AccountService;
import com.bankingSystem.bankingSystem.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

   private final CustomerService customerService;
   private final AccountService accountService;

   public CustomerController(CustomerService customerService, AccountService accountService){
      this.customerService = customerService;
      this.accountService = accountService;
   }

   @GetMapping("/")
   public ResponseEntity<List<Customer>> getAllCustomers(){
      return customerService.getAllCustomers();
   }

   @GetMapping("/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id){
      return customerService.findById(id);
   }

   @PostMapping("/turnover")
   public ResponseEntity<List<Account>> getTurnovers(){
      return accountService.getTurnovers();
   }

   @PostMapping("/create")
   public ResponseEntity<Customer> createCustomer(@RequestBody JsonNode in){
      return customerService.createCustomer(in);
   }

}
