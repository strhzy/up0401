package com.strhzy.api.service;

import com.strhzy.api.model.Product;
import com.strhzy.api.model.Review;
import com.strhzy.api.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .filter(r -> r.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    }
}
