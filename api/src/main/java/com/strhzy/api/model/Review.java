package com.strhzy.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Товар обязателен")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @NotNull(message = "Покупатель обязателен")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @NotNull(message = "Рейтинг обязателен")
    @Min(value = 1, message = "Минимальный рейтинг — 1")
    @Max(value = 5, message = "Максимальный рейтинг — 5")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Комментарий обязателен")
    @Column
    private String comment;

    @NotNull(message = "Дата создания обязательна")
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
