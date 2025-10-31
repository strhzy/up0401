package com.strhzy.finalpr.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Manufacturer {
    private Long id;

    @NotBlank(message = "Название производителя обязательно")
    private String name;

    private String description;
    private String country;
}
