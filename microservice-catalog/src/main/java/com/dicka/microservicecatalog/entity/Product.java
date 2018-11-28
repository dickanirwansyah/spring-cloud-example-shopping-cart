package com.dicka.microservicecatalog.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
public class Product implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @NotBlank
    private String name;
    private String description;
    private double price;

    @Transient
    private boolean inStock = true;
}
