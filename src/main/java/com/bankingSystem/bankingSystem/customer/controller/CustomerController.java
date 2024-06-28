package com.bankingSystem.bankingSystem.customer.controller;

import com.bankingSystem.bankingSystem.customer.dto.CustomerDto;
import com.bankingSystem.bankingSystem.customer.service.CustomerService;
import com.bankingSystem.bankingSystem.account.entity.Account;
import com.bankingSystem.bankingSystem.customer.entity.Customer;
import com.bankingSystem.bankingSystem.account.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

   @PostMapping("/")
   @Parameter(name = "name", example = "Pero Peric", required = true)
   @Parameter(name = "address", example = "Example Address 19", required = true)
   @Parameter(name = "email", example = "test@test.com", required = true)
   @Parameter(name = "phoneNumber", example = "0992699646", required = true)
   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer to add", required = true, content = @Content(schema = @Schema(implementation = CustomerDto.class)))
   public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDto in){
      return customerService.createCustomer(in);
   }

}
