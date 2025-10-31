package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Product;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private static final String BASE_PATH = "/products";

    public List<Product> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка товаров", e);
        }
    }

    public Optional<Product> findById(Long id) {
        try {
            Product product = HttpService.get(BASE_PATH + "/" + id, Product.class);
            return Optional.ofNullable(product);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении товара с ID: " + id, e);
        }
    }

    public Product save(Product product) {
        try {
            if (product.getId() == null) {
                return HttpService.post(BASE_PATH, product, Product.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + product.getId(), product, Product.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении товара", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении товара с ID: " + id, e);
        }
    }
}