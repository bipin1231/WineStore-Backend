package com.winestore.winestore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String size;
    private Integer bottleInCartoon;
    @OneToMany(mappedBy = "size")
    private List<ProductVariant> productVariant;
}
