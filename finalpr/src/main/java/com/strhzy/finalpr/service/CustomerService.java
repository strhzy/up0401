package com.strhzy.finalpr.service;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public void register(String username, String password) {
        Customer customer = new Customer();
        customer.setName(username);
        customer.setUsername(username);
        customer.setPassword(password);
        save(customer);
    }
}