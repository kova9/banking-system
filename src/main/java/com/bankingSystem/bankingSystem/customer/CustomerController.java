package com.bankingSystem.bankingSystem.customer;

import com.bankingSystem.bankingSystem.account.Account;
import com.bankingSystem.bankingSystem.account.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
class CustomerController {

   private final CustomerService customerService;

   public CustomerController(CustomerService customerService){
      this.customerService = customerService;
   }

   @GetMapping("/")
   public ResponseEntity<List<Customer>> getAllCustomers(){
      return customerService.getAllCustomers();
   }

   @GetMapping("/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id){
      return customerService.findById(id);
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
