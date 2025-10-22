package com.strhzy.dbproj.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Year {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Год обязателен")
    @Min(value = 0, message = "Некорректный год")
    private Integer value;

    @OneToMany(mappedBy = "year", cascade = {})
    private List<Book> books;
}
