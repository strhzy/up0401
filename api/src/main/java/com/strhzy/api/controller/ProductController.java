package com.strhzy.api.controller;

import com.strhzy.api.dto.ProductDto;
import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Category;
import com.strhzy.api.model.Manufacturer;
import com.strhzy.api.model.Product;
import com.strhzy.api.repository.CategoryRepository;
import com.strhzy.api.repository.ManufacturerRepository;
import com.strhzy.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "Операции с товарами")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryRepository categoryRepository,
                             ManufacturerRepository manufacturerRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все товары с фильтрацией")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "") String sort
    ) {
        List<Product> products = productService.findAllFiltered(categoryId, manufacturerId, q, sort);
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + id + " не найден"));
        return ResponseEntity.ok(toDto(product));
    }

    @PostMapping
    @Operation(summary = "Создать новый товар")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категория с ID " + dto.getCategoryId() + " не найдена"));
        Manufacturer manufacturer = manufacturerRepository.findById(dto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Производитель с ID " + dto.getManufacturerId() + " не найден"));

        Product product = fromDto(dto);
        product.setCategory(category);
        product.setManufacturer(manufacturer);

        Product saved = productService.save(product);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + id + " не найден"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категория с ID " + dto.getCategoryId() + " не найдена"));
        Manufacturer manufacturer = manufacturerRepository.findById(dto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Производитель с ID " + dto.getManufacturerId() + " не найден"));

        Product product = fromDto(dto);
        product.setId(id);
        product.setCategory(category);
        product.setManufacturer(manufacturer);

        Product saved = productService.save(product);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + id + " не найден"));
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        if (p.getCategory() != null) dto.setCategoryId(p.getCategory().getId());
        if (p.getManufacturer() != null) dto.setManufacturerId(p.getManufacturer().getId());
        return dto;
    }

    private Product fromDto(ProductDto dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        return p;
    }
}