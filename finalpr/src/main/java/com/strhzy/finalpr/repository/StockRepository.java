package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Stock;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class StockRepository {

    private static final String BASE_PATH = "/stocks";

    public List<Stock> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка остатков", e);
        }
    }

    public Optional<Stock> findById(Long id) {
        try {
            Stock stock = HttpService.get(BASE_PATH + "/" + id, Stock.class);
            return Optional.ofNullable(stock);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении остатка с ID: " + id, e);
        }
    }

    public Stock save(Stock stock) {
        try {
            if (stock.getId() == null) {
                return HttpService.post(BASE_PATH, stock, Stock.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + stock.getId(), stock, Stock.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении остатка", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении остатка с ID: " + id, e);
        }
    }
}