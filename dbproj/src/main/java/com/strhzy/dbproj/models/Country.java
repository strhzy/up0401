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
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Название страны не может быть пустым")
    private String name;
    @OneToMany(mappedBy = "country", cascade = {})
    private List<Book> books;
    @OneToMany(mappedBy = "country", cascade = {})
    private List<Author> authors;
}
