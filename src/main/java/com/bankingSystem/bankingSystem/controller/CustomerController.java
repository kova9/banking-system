package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Account;
import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.service.AccountService;
import com.bankingSystem.bankingSystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

   private final CustomerService provider;
   private final AccountService accountService;

   public CustomerController(CustomerService provider, AccountService accountService){
      this.provider = provider;
      this.accountService = accountService;
   }

   @GetMapping("/all")
   public ResponseEntity<List<Customer>> getAllCustomers(){
      return provider.getAllCustomers();
   }

   @GetMapping("/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id){
      return provider.findById(id);
   }

   @PostMapping("/turnover")
   public ResponseEntity<List<Account>> getTurnovers(){
      return accountService.getTurnovers();
   }

}
