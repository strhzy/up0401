package com.strhzy.api.controller;

import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Customer;
import com.strhzy.api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Controller", description = "Операции с покупателями")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Получить всех покупателей")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить покупателя по ID")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + id + " не найден"));
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @Operation(summary = "Создать нового покупателя")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.save(customer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить покупателя")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        customerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + id + " не найден"));
        customer.setId(id);
        return ResponseEntity.ok(customerService.save(customer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить покупателя")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + id + " не найден"));
        customerService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
