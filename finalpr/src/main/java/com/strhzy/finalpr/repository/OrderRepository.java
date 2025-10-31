package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Order;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private static final String BASE_PATH = "/orders";

    public List<Order> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка заказов", e);
        }
    }

    public Optional<Order> findById(Long id) {
        try {
            Order order = HttpService.get(BASE_PATH + "/" + id, Order.class);
            return Optional.ofNullable(order);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении заказа с ID: " + id, e);
        }
    }

    public Order save(Order order) {
        try {
            if (order.getId() == null) {
                return HttpService.post(BASE_PATH, order, Order.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + order.getId(), order, Order.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении заказа", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении заказа с ID: " + id, e);
        }
    }
}