package com.strhzy.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название производителя обязательно")
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String country;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
}
