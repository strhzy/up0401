package com.strhzy.finalpr.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItem {
    private Long id;

    @NotNull(message = "ID заказа обязателен")
    private Long orderId;

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Количество должно быть больше 0")
    private Integer quantity;

    @NotNull(message = "Цена обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Double price;
}