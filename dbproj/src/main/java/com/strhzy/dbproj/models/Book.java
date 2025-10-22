package com.strhzy.dbproj.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Название книги не может быть пустым")
    private String title;

    @Min(value = 1, message = "Количество страниц должно быть больше 0")
    private Integer pages;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Author author;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "year_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Year year;
    @ManyToOne
    @JoinColumn(name = "country_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Country country;
}
