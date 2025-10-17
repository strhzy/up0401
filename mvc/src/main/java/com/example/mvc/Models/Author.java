package com.example.mvc.Models;

import jakarta.validation.constraints.*;

public class Author {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 100, message = "Имя не должно превышать 100 символов")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
    private String surname;

    @Size(max = 100, message = "Отчество не должно превышать 100 символов")
    private String lastname;

    public Author() {
    }

    public Author(Long id, String name, String surname, String lastname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}