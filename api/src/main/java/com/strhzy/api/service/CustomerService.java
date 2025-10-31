package com.strhzy.api.service;

import com.strhzy.api.model.Customer;
import com.strhzy.api.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository repo, PasswordEncoder encoder) {
        this.customerRepository = repo;
        this.passwordEncoder = encoder;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer register(String username, String password) {
        if (customerRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        Customer user = new Customer();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return customerRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer u = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole())
                .build();
    }
}
