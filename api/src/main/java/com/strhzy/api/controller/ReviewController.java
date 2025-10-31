package com.strhzy.api.controller;

import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Review;
import com.strhzy.api.dto.ReviewDto;
import com.strhzy.api.model.Product;
import com.strhzy.api.model.Customer;
import com.strhzy.api.repository.ProductRepository;
import com.strhzy.api.repository.CustomerRepository;
import com.strhzy.api.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review Controller", description = "Операции с отзывами")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ProductRepository productRepository,
                            CustomerRepository customerRepository) {
        this.reviewService = reviewService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public List<Review> getAllReviews(@RequestParam(required = false) Long productId) {
        if (productId != null) {
            return reviewService.findByProductId(productId);
        }
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв с ID " + id + " не найден"));
        return ResponseEntity.ok(review);
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + dto.getProductId() + " не найден"));
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + dto.getCustomerId() + " не найден"));

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return ResponseEntity.ok(reviewService.save(review));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDto dto) {
        Review existing = reviewService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв с ID " + id + " не найден"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + dto.getProductId() + " не найден"));
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + dto.getCustomerId() + " не найден"));

        existing.setProduct(product);
        existing.setCustomer(customer);
        existing.setRating(dto.getRating());
        existing.setComment(dto.getComment());

        return ResponseEntity.ok(reviewService.save(existing));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв с ID " + id + " не найден"));
        reviewService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
