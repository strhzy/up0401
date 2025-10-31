package com.strhzy.finalpr.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Stock {
    private Long id;

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotNull(message = "Количество обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer quantity;

    private String location;
}
