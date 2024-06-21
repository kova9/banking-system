package com.bankingSystem.bankingSystem.controller;

import com.bankingSystem.bankingSystem.dataaccess.entity.Customer;
import com.bankingSystem.bankingSystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

   private final CustomerService provider;

   public CustomerController(CustomerService provider){
      this.provider = provider;
   }

   @GetMapping("/all")
   public ResponseEntity<List<Customer>> getAllCustomers(){
      return provider.getAllCustomers();
   }

   @GetMapping("/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id){
      return provider.findById(id);
   }

}
