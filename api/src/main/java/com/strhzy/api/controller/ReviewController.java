package com.strhzy.api.controller;

import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Review;
import com.strhzy.api.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review Controller", description = "Операции с отзывами")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public List<Review> getAllReviews() {
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
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.save(review));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody Review review) {
        reviewService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв с ID " + id + " не найден"));
        review.setId(id);
        return ResponseEntity.ok(reviewService.save(review));
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
