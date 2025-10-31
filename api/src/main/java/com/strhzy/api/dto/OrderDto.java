package com.strhzy.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    private String status;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    private List<OrderItemDto> orderItems;
}
