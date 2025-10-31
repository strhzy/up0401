package com.strhzy.finalpr.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;

    @NotNull(message = "Покупатель обязателен")
    private Long customerId;

    @NotNull(message = "Дата заказа обязательна")
    private LocalDateTime orderDate;

    private String status;

    @NotNull(message = "Сумма заказа обязательна")
    private Double totalAmount;
}