package com.strhzy.api.controller;

import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Manufacturer;
import com.strhzy.api.service.ManufacturerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@Tag(name = "Manufacturer Controller", description = "Операции с производителями")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    @Operation(summary = "Получить всех производителей")
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить производителя по ID")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Производитель с ID " + id + " не найден"));
        return ResponseEntity.ok(manufacturer);
    }

    @PostMapping
    @Operation(summary = "Создать нового производителя")
    public ResponseEntity<Manufacturer> createManufacturer(@Valid @RequestBody Manufacturer manufacturer) {
        return ResponseEntity.ok(manufacturerService.save(manufacturer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить производителя")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable Long id, @Valid @RequestBody Manufacturer manufacturer) {
        manufacturerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Производитель с ID " + id + " не найден"));
        manufacturer.setId(id);
        return ResponseEntity.ok(manufacturerService.save(manufacturer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить производителя")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        manufacturerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Производитель с ID " + id + " не найден"));
        manufacturerService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
