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
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Название жанра не может быть пустым")
    private String name;

    @OneToMany(mappedBy = "genre", cascade = {})
    private List<Book> books;
}
