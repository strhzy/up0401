package com.strhzy.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReviewDto {

    @NotNull(message = "Товар обязателен")
    private Long productId;

    @NotNull(message = "Покупатель обязателен")
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
