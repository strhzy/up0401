package com.strhzy.dbproj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    private String lastname;
    @OneToMany(mappedBy = "author", cascade = {})
    private List<Book> books;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}