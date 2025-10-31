package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private static final String BASE_PATH = "/customers";

    public List<Customer> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка покупателей", e);
        }
    }

    public Optional<Customer> findById(Long id) {
        try {
            Customer customer = HttpService.get(BASE_PATH + "/" + id, Customer.class);
            return Optional.ofNullable(customer);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении покупателя с ID: " + id, e);
        }
    }

    public Customer save(Customer customer) {
        try {
            if (customer.getId() == null) {
                return HttpService.post(BASE_PATH, customer, Customer.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + customer.getId(), customer, Customer.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении покупателя", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении покупателя с ID: " + id, e);
        }
    }
}