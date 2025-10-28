package com.strhzy.api.controller;

import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Stock;
import com.strhzy.api.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stock Controller", description = "Операции с остатками на складе")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @Operation(summary = "Получить все остатки товаров")
    public List<Stock> getAllStocks() {
        return stockService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить остаток по ID")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Stock stock = stockService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись об остатке с ID " + id + " не найдена"));
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    @Operation(summary = "Создать новую запись об остатке")
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.save(stock));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись об остатке")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @Valid @RequestBody Stock stock) {
        stockService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись с ID " + id + " не найдена"));
        stock.setId(id);
        return ResponseEntity.ok(stockService.save(stock));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись об остатке")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись с ID " + id + " не найдена"));
        stockService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
