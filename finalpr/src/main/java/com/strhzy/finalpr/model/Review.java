package com.strhzy.finalpr.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotNull(message = "ID покупателя обязателен")
    private Long customerId;

    @NotNull(message = "Рейтинг обязателен")
    @Min(value = 1, message = "Минимальный рейтинг — 1")
    @Max(value = 5, message = "Максимальный рейтинг — 5")
    private Integer rating;

    @NotBlank(message = "Комментарий обязателен")
    private String comment;

    @NotNull(message = "Дата создания обязательна")
    private Timestamp createdAt;
}
