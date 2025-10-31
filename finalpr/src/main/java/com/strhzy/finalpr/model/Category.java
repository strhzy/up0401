package com.strhzy.finalpr.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Category {
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    private String name;

    private String description;
}
