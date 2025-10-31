package com.strhzy.finalpr.service;

import com.strhzy.finalpr.model.Stock;
import com.strhzy.finalpr.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }
}