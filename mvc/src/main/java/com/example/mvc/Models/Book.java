package com.example.mvc.Models;

import jakarta.validation.constraints.*;

public class Book {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @NotNull(message = "Требуется указать id автора")
    private Long author_id;

    @NotNull(message = "Требуется указать количество страниц")
    @Min(value = 1, message = "Количество страниц должно быть не меньше 1")
    private Integer pageCount;

    public Book() {
    }

    public Book(Long id, String title, Long author_id, Integer pageCount) {
        this.id = id;
        this.title = title;
        this.author_id = author_id;
        this.pageCount = pageCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}