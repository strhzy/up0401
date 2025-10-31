package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Review;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    private static final String BASE_PATH = "/reviews";

    public List<Review> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка отзывов", e);
        }
    }

    public Optional<Review> findById(Long id) {
        try {
            Review review = HttpService.get(BASE_PATH + "/" + id, Review.class);
            return Optional.ofNullable(review);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении отзыва с ID: " + id, e);
        }
    }

    public Review save(Review review) {
        try {
            if (review.getId() == null) {
                return HttpService.post(BASE_PATH, review, Review.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + review.getId(), review, Review.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении отзыва", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении отзыва с ID: " + id, e);
        }
    }
}