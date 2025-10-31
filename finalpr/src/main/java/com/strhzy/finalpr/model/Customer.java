package com.strhzy.finalpr.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Customer {
    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Email обязателен")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    private String address;
    private String phone;
    private String role = "USER";
}