package com.strhzy.api.service;

import com.strhzy.api.model.Product;
import com.strhzy.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAllFiltered(Long categoryId, Long manufacturerId, String q, String sort) {
        List<Product> products = findAll();

        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        if (manufacturerId != null) {
            products = products.stream()
                    .filter(p -> p.getManufacturer().getId().equals(manufacturerId))
                    .collect(Collectors.toList());
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(query)
                            || p.getDescription().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "price_asc" -> products.sort(Comparator.comparing(Product::getPrice));
                case "price_desc" -> products.sort(Comparator.comparing(Product::getPrice).reversed());
                case "name_asc" -> products.sort(Comparator.comparing(Product::getName));
                case "name_desc" -> products.sort(Comparator.comparing(Product::getName).reversed());
            }
        }

        return products;
    }

}
