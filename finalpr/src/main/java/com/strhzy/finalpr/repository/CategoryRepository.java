package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Category;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private static final String BASE_PATH = "/categories";

    public List<Category> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка категорий", e);
        }
    }

    public Optional<Category> findById(Long id) {
        try {
            Category category = HttpService.get(BASE_PATH + "/" + id, Category.class);
            return Optional.ofNullable(category);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении категории с ID: " + id, e);
        }
    }

    public Category save(Category category) {
        try {
            if (category.getId() == null) {
                return HttpService.post(BASE_PATH, category, Category.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + category.getId(), category, Category.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении категории", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении категории с ID: " + id, e);
        }
    }
}