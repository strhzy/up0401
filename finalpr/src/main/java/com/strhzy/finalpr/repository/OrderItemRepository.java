package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.OrderItem;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemRepository {

    private static final String BASE_PATH = "/order-items";

    public List<OrderItem> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка позиций заказов", e);
        }
    }

    public Optional<OrderItem> findById(Long id) {
        try {
            OrderItem orderItem = HttpService.get(BASE_PATH + "/" + id, OrderItem.class);
            return Optional.ofNullable(orderItem);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении позиции заказа с ID: " + id, e);
        }
    }

    public OrderItem save(OrderItem orderItem) {
        try {
            if (orderItem.getId() == null) {
                return HttpService.post(BASE_PATH, orderItem, OrderItem.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + orderItem.getId(), orderItem, OrderItem.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении позиции заказа", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении позиции заказа с ID: " + id, e);
        }
    }
}